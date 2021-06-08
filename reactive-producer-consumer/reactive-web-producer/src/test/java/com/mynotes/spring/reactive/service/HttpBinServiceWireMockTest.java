package com.mynotes.spring.reactive.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpBinServiceWireMockTest {

    private WireMockServer wireMockServer;
    private HttpBinService httpBinService;

    @BeforeEach
    public void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        httpBinService = new HttpBinService(wireMockServer.baseUrl());
    }
    @AfterEach
    public void stopWireMock() {
        wireMockServer.stop();
        wireMockServer = null;
    }

    @Test
    public void binCall_success() {

        wireMockServer
                .stubFor(
                        WireMock.get("/anything").withHeader("headerKey", equalTo("headerValue")).willReturn(
                                okJson("test")));

        Mono<String> result = httpBinService.binCall();

        StepVerifier.create(result)
                .expectNextMatches(res -> res.equalsIgnoreCase("test"))
                .verifyComplete();
    }

    @Test
    public void binCall_header_check_not_found() {

        wireMockServer
                .stubFor(
                        WireMock.get("/anything").withHeader("invalid", equalTo("invalid")).willReturn(
                                okJson("test")));

        Mono<String> result = httpBinService.binCall();

        StepVerifier.create(result)
                .expectErrorMessage("not_found");
    }

}