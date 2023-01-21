package com.example.gateway.configuration;


import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SecurityConfigurationIT {

    protected static int mockServerPort;
    protected WebTestClient webClient;
    protected ClientAndServer mockServer;
    private static final String CSRF_HEADER = "X-XSRF-TOKEN";
    private static final String CSRF_COOKIE = "XSRF-TOKEN";
    private static final String AUTHORIZATION = "Authorization";
    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeClass() {
        mockServerPort = PortFactory.findFreePort();
        System.setProperty("mock.server.port", String.valueOf(mockServerPort));
    }

    @AfterAll
    public static void afterClass() {
        System.clearProperty("mock.server.port");
    }

    @BeforeEach
    void setUp() {
        webClient = WebTestClient.bindToServer().responseTimeout(Duration.ofSeconds(60))
                .baseUrl("http://localhost:" + port).build();
        mockServer = ClientAndServer.startClientAndServer(mockServerPort);
        mockServer.when(request().withPath("/test"))
                .respond(response().withStatusCode(200));

    }

    @AfterEach
    public void tearDown() {
        webClient.delete();
        mockServer.stop();
    }

    @Test
    void given_postRequest_withAuthHeader_andNoCsrfHeaderAndCookie_then_expectOk_and_CsrfCookie() {
        webClient.post().uri("/test")
                .header(AUTHORIZATION, AUTHORIZATION)
                .exchange()
                .expectStatus().isOk()
                .expectCookie().exists(CSRF_COOKIE);
    }

    @Test
    void given_postRequest_withAuthCookie_andNoCsrfHeaderAndCookie_then_expectOk_and_CsrfCookie() {
        webClient.post().uri("/test")
                .cookie(AUTHORIZATION, AUTHORIZATION)
                .exchange()
                .expectStatus().isForbidden()
                .expectCookie().doesNotExist(CSRF_COOKIE);
    }



    @Test
    void given_postRequest_withAuthHeader_andCsrfHeaderAndCookie_then_expectOk_and_noCsrfCookie() {
        webClient.post().uri("/test")
                .header(AUTHORIZATION, AUTHORIZATION)
                .header(CSRF_HEADER, "asd123")
                .cookie(CSRF_COOKIE, "asd123")
                .exchange()
                .expectStatus().isOk()
                .expectCookie().doesNotExist(CSRF_COOKIE);
    }

    @Test
    void given_postRequest_withAuthCookie_andCsrfHeaderAndCookie_then_expectOk_and_noCsrfCookie() {
        webClient.post().uri("/test")
                .cookie(AUTHORIZATION, AUTHORIZATION)
                .header(CSRF_HEADER, "asd123")
                .cookie(CSRF_COOKIE, "asd123")
                .exchange()
                .expectStatus().isOk()
                .expectCookie().doesNotExist(CSRF_COOKIE);
    }


}
