package com.mynotes.spring.reactive.controller;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.mynotes.spring.reactive.Application;
import com.mynotes.spring.reactive.service.HttpBinService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Slf4j
public class HttpBinControllerMockWebServerTest {

    @Autowired
    WebTestClient webTestClient;

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        // Use Dynamic Properties register instead of this
        //System.setProperty("my.service.url", String.format("http://localhost:%s",mockBackEnd.getPort()));
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
    public void httpBinAnything() {

        mockBackEnd.enqueue(new MockResponse().setBody("test").setResponseCode(200));

        //Method 1
        webTestClient.get().uri("/httpbin/call")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("test")
        ;
    }

    @Test
    public void httpBinAnything_with_Dispatcher() {

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                switch (request.getPath()) {
                    case "/anything":
                        return new MockResponse().setResponseCode(200).setBody("test");
                    case "/users/2":
                        return new MockResponse().setResponseCode(500);
                    case "/users/3":
                        return new MockResponse().setResponseCode(200).setBody("{\"id\": 1, \"name\":\"duke\"}");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockBackEnd.setDispatcher(dispatcher);

        //Method 1
        webTestClient.get().uri("/httpbin/call")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("test")
        ;
    }
}
