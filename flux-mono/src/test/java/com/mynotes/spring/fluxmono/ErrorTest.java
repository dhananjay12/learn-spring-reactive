package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

public class ErrorTest {
    @Test
    public void fluxTestAndErrorTest() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Error123")))
                .concatWith(Flux.just("e")).log();

        StepVerifier.create(myFlux)
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                //.expectError(RuntimeException.class) //Cannot have both together
                .expectErrorMessage("Error123")
                .verify();

    }

    @Test
    public void fluxError_onErrorResume() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Error123")))
                .concatWith(Flux.just("e"))
                .onErrorResume( e -> {
                    System.out.println("Something broke"+ e.getMessage());
                    return Flux.just("default");
                }).log();

        StepVerifier.create(myFlux)
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .expectNext("default")// <==
                .verifyComplete();

    }

    @Test
    public void fluxError_onErrorReturn() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Error123")))
                .concatWith(Flux.just("e"))
                .onErrorReturn("default").log();

        StepVerifier.create(myFlux)
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .expectNext("default")// <==
                .verifyComplete();

    }

    @Test
    public void fluxError_onErrorMap() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Error123")))
                .concatWith(Flux.just("e"))
                .onErrorMap(e -> new IllegalArgumentException("Check input"))
                .log();

        StepVerifier.create(myFlux)
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .expectError(IllegalArgumentException.class)
                .verify();

    }

    @Test
    public void fluxError_Retry() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Error123")))
                .concatWith(Flux.just("e"))
                .retry(1)
                .log();

        StepVerifier.create(myFlux)
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    public void fluxError_RetryBackOff() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
                .concatWith(Flux.error(new RuntimeException("Error123")))
                .concatWith(Flux.just("e"))
                .retryWhen(Retry.backoff(1,Duration.ofSeconds(5)))
                .log();

        StepVerifier.create(myFlux)
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectError(RuntimeException.class)
                .verify();

    }



}
