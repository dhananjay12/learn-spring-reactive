package com.mynotes.spring.reactive.service;


import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

class HttpBinServiceMockWebServerTest {

    public static MockWebServer mockBackEnd;

    private HttpBinService httpBinService;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        httpBinService = new HttpBinService(baseUrl);
    }

    @Test
    public void binCallTest(){
        mockBackEnd.enqueue(new MockResponse().setBody("test").setResponseCode(200));

        Mono<String> result = httpBinService.binCall();

        StepVerifier.create(result)
                .expectNextMatches(res -> res.equalsIgnoreCase("test"))
                .verifyComplete();
    }

    @Test
    public void binCallTest_invalid(){
        mockBackEnd.enqueue(new MockResponse().setBody("test1").setResponseCode(200));

        Mono<String> result = httpBinService.binCall();

        StepVerifier.create(result)
                .expectNextMatches(res -> !res.equalsIgnoreCase("test"))
                .verifyComplete();
    }


}