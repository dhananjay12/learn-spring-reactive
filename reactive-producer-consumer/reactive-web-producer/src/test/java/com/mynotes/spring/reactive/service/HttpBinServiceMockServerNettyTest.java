package com.mynotes.spring.reactive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class HttpBinServiceMockServerNettyTest {

    private static int mockTestServerPort;
    protected ClientAndServer mockTestServer;
    private HttpBinService httpBinService;

    @BeforeAll
    public static void beforeClass() {
        mockTestServerPort = PortFactory.findFreePort();
    }

    @BeforeEach
    public void setUp() {
        mockTestServer = ClientAndServer.
                startClientAndServer(mockTestServerPort);
        httpBinService = new HttpBinService("http://localhost:" + mockTestServer.getPort());
    }

    @AfterEach
    public void cleanup() {
        mockTestServer.stop();
    }

    @Test
    public void bindCall_success() {
        mockTestServer
                .when(request()
                        .withPath("/anything")
                        .withHeader("headerKey", "headerValue"))
                .respond(
                        response().withStatusCode(200)
                                .withBody("test"));

        Mono<String> result = httpBinService.binCall();

        StepVerifier.create(result)
                .expectNextMatches(res -> res.equalsIgnoreCase("test"))
                .verifyComplete();

    }

    @Test
    public void binCall_header_check_not_found() {
        mockTestServer
                .when(request()
                        .withPath("/anything")
                        .withHeader("invalid", "invalid"))
                .respond(
                        response().withStatusCode(404));

        Mono<String> result = httpBinService.binCall();

        StepVerifier.create(result)
                .expectErrorMessage("not_found");

    }
}
