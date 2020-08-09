package com.mynotes.spring.reactive.router;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class RouterTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxTest() {

        // Method 1
        Flux<Integer> intergerFlux = webTestClient.get().uri("/functional/flux")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(Integer.class)
            .getResponseBody();

        StepVerifier.create(intergerFlux)
            .expectSubscription()
            .expectNext(1)
            .expectNext(2)
            .expectNext(3)
            .expectNext(4)
            .verifyComplete();
    }


}
