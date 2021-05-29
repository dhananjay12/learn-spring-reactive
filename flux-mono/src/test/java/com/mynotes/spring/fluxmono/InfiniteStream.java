package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class InfiniteStream {

    @Test
    public void infiniteStream() throws InterruptedException {
        Flux<Long> infinite = Flux.interval(Duration.ofMillis(100)).log();

        infinite.subscribe(element -> System.out.println("Element :: "+element));

        Thread.sleep(3000);

    }

    @Test
    public void infiniteStreamTest() throws InterruptedException {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100))
                .take(3).log();

        StepVerifier.create(finiteFlux)
                .expectNext(0l,1l,2l)
                .verifyComplete();

    }
}
