package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FluxAndMonoFactoryMethods {

    @Test
    public void fluxFromIterable(){
        List<String> myList = List.of("a","b","c");

        Flux<String> flux = Flux.fromIterable(myList).log();

        StepVerifier.create(flux)
                .expectNext("a","b","c")
                .verifyComplete();

    }

    @Test
    public void fluxFromArray(){
        String[] myArr = new String[]{"a","b","c"};

        Flux<String> flux = Flux.fromArray(myArr).log();

        StepVerifier.create(flux)
                .expectNext("a","b","c")
                .verifyComplete();

    }

    @Test
    public void fluxFromStream(){
        Stream<String> stream = List.of("a","b","c").stream();

        Flux<String> flux = Flux.fromStream(stream).log();

        StepVerifier.create(flux)
                .expectNext("a","b","c")
                .verifyComplete();

    }

    @Test
    public void fluxFromRange(){

        Flux<Integer> flux = Flux.range(1,10);

        StepVerifier.create(flux)
                .expectNextCount(10)
                .verifyComplete();

    }

    @Test
    public void monoFromJustOrEmpty(){

        Mono<String> mono =Mono.justOrEmpty(null);

        StepVerifier.create(mono.log())
                .verifyComplete();

    }

    @Test
    public void monoFromSupplier(){

        Supplier<String> supplier = () -> "a";

        Mono<String> mono =Mono.fromSupplier(supplier);

        StepVerifier.create(mono.log())
                .expectNext("a")
                .verifyComplete();

    }



}
