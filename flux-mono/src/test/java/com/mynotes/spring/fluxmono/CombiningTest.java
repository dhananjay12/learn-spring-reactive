package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

public class CombiningTest {

    @Test
    public void combineUsingMerge(){

        Flux<String> flux1 = Flux.just("a","b","c");
        Flux<String> flux2 = Flux.just("d","e","f");


        Flux<String> finalFlux = Flux.merge(flux1,flux2).log();

        StepVerifier.create(finalFlux)
                .expectNext("a","b","c","d","e","f")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_delay(){

        Flux<String> flux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("d","e","f").delayElements(Duration.ofSeconds(1));


        Flux<String> finalFlux = Flux.merge(flux1,flux2).log();

        StepVerifier.create(finalFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingContact(){

        Flux<String> flux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("d","e","f").delayElements(Duration.ofSeconds(1));


        Flux<String> finalFlux = Flux.concat(flux1,flux2).log();

        StepVerifier.create(finalFlux)
                .expectNext("a","b","c","d","e","f")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip(){

        Flux<String> flux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("d","e","f").delayElements(Duration.ofSeconds(2));


        Flux<String> finalFlux = Flux.zip(flux1,flux2, (t1, t2) -> {
            return t1.concat(t2);
        }).log();

        StepVerifier.create(finalFlux)
                .expectNext("ad","be","cf")
                .verifyComplete();
    }
}
