package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

public class FilterTest {

    @Test
    public void fluxFilterTest(){
        List<String> myList = List.of("foo","bar","baz");

        Flux<String> flux = Flux.fromIterable(myList)
                .filter(s -> s.startsWith("ba"))
                .log();

        StepVerifier.create(flux)
                .expectNext("bar", "baz")
                .verifyComplete();
    }
}
