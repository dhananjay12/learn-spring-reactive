package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void withoutVirtualTime() throws InterruptedException {
        TestUtils.startTimer();
        Flux<String> myFlux = Flux.just("a", "b", "c", "d", "e", "f")
                .delayElements(Duration.ofSeconds(1))
                .log();

        StepVerifier.create(myFlux)
                .expectNextCount(6)
                .verifyComplete();
        TestUtils.stopTimer();

    }

    @Test
    public void withVirtualTime() throws InterruptedException {
        VirtualTimeScheduler.getOrSet();
        TestUtils.startTimer();
        Flux<String> myFlux = Flux.just("a", "b", "c", "d", "e", "f")
                .delayElements(Duration.ofSeconds(1))
                .log();

        StepVerifier.withVirtualTime(() -> myFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNextCount(6)
                .verifyComplete();
        TestUtils.stopTimer();

    }


}
