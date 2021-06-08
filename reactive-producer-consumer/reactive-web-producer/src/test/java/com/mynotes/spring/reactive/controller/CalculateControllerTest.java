package com.mynotes.spring.reactive.controller;

import com.mynotes.spring.reactive.dto.CalculateResponseDTO;
import com.mynotes.spring.reactive.service.CalculateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CalculateControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxTest(){

        // Method 1
        Flux<CalculateResponseDTO> result = webTestClient.get().uri("/calculate/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CalculateResponseDTO.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        //Method 2
        webTestClient.get().uri("/calculate/9")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.input").isNotEmpty()
                .jsonPath("$.square").isEqualTo(81)
                .jsonPath("$.sqrt").isEqualTo(3)
        ;
    }
}
