package com.mynotes.reactive.demo.webfluxinservlet.controller;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebfluxInServletApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Slf4j
public class HttpBinControllerIT {

    @Autowired
    WebTestClient webTestClient;

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("my.service.url",
                () -> String.format("http://localhost:%s",mockBackEnd.getPort()));
    }


    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void webClientGet_200() {

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                switch (request.getPath()) {
                    case "/get":
                        return new MockResponse().setResponseCode(200).setBody("test");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockBackEnd.setDispatcher(dispatcher);

        webTestClient.get().uri("/bin/web/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("test");
    }

    @Test
    public void webClientGet_4xx() {

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                switch (request.getPath()) {
                    case "/get":
                        return new MockResponse().setResponseCode(417).setBody("Expectation failed");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockBackEnd.setDispatcher(dispatcher);

        webTestClient.get().uri("/bin/web/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                //Because of exception handling its 4xx else it would be 500 because we are putting mono.error
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("Client 4xx error");
    }

    @Test
    public void webClientGet_5xx() {

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                switch (request.getPath()) {
                    case "/get":
                        return new MockResponse().setResponseCode(500).setBody("Server error");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockBackEnd.setDispatcher(dispatcher);

        webTestClient.get().uri("/bin/web/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                //In exception handling we are setting the same status what we got from call
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Client 5xx error");
    }

    @Test
    public void restTemplateGet_200() {

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                switch (request.getPath()) {
                    case "/get":
                        return new MockResponse().setResponseCode(200).setBody("test");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockBackEnd.setDispatcher(dispatcher);

        webTestClient.get().uri("/bin/rest/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("test")
        ;
    }

    @Test
    public void restTemplateGet_4xx() {

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                switch (request.getPath()) {
                    case "/get":
                        return new MockResponse().setResponseCode(417).setBody("Expectation failed");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockBackEnd.setDispatcher(dispatcher);

        webTestClient.get().uri("/bin/rest/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                //Because of exception handling its 4xx else it would be 500 because we are putting mono.error
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("Client 4xx error rest template")
        ;
    }

    @Test
    public void restTemplateGet_5xx() {

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                switch (request.getPath()) {
                    case "/get":
                        return new MockResponse().setResponseCode(500).setBody("Server error");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockBackEnd.setDispatcher(dispatcher);

        webTestClient.get().uri("/bin/rest/get")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                //In exception handling we are setting the same status what we got from call
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Server 5xx error rest template")
        ;
    }

}
