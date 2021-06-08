package com.mynotes.spring.reactive.controller;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.mynotes.spring.reactive.Application;
import com.mynotes.spring.reactive.service.HttpBinService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {"my.service.url="+ HttpBinControllerTest.externalUri})
@Slf4j
public class HttpBinControllerTest {

    static final String externalUri = "http://localhost:12345";

    @Autowired
    WebTestClient webTestClient;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(12345));
        wireMockServer.start();
    }
    @AfterEach
    public void stopWireMock() {
        wireMockServer.stop();
        wireMockServer = null;
    }

    @Test
    public void httpBinAnything() {

                wireMockServer
                .stubFor(
                        WireMock.get("/anything").willReturn(ok("test")));

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
