package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

class FluxMonoApplicationTest {

    @Test
    public void flux() {
        Flux<String> myFlux = Flux.just("a", "b", "c");

        myFlux.subscribe(System.out::println);

    }

    @Test
    public void fluxTest() {
        Flux<String> myFlux = Flux.just("a", "b", "c").log();

        StepVerifier.create(myFlux)
            .expectNext("a")
            .expectNext("b")
            .expectNext("c")
            .verifyComplete();

        StepVerifier.create(myFlux)
            .expectNext("a","b", "c")
            .verifyComplete();

        StepVerifier.create(myFlux)
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    public void fluxTestOnComplete() {
        Flux<String> myFlux = Flux.just("a", "b", "c");

        myFlux.subscribe(System.out::println,
            (e) -> System.err.println(e),
            () -> System.out.println("Completed"));

    }

    @Test
    public void fluxTestAndError() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
            .concatWith(Flux.error(new RuntimeException("Error123"))).log();

        myFlux.subscribe(System.out::println,
            (e) -> System.err.println(e));

    }

    @Test
    public void fluxTestAndErrorTest() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
            .concatWith(Flux.error(new RuntimeException("Error123"))).log();

        StepVerifier.create(myFlux)
            .expectNext("a")
            .expectNext("b")
            .expectNext("c")
            //.expectError(RuntimeException.class) //Cannot have both together
            .expectErrorMessage("Error123")
            .verify();

    }

    @Test
    public void fluxTestAndErrorAndFLux() {
        Flux<String> myFlux = Flux.just("a", "b", "c")
            .concatWith(Flux.error(new RuntimeException("Error")))
            .concatWith(Flux.just("After Error"))
            .log();

        myFlux.subscribe(System.out::println,
            (e) -> System.err.println(e));

    }

    @Test
    public void monoTest(){
        Mono<String> myMono = Mono.just("a").log();

        StepVerifier.create(myMono)
            .expectNext("a")
            .verifyComplete();
    }

     @Test
    public void monoErrorTest(){

         Mono<Object> myMono = Mono.error(new RuntimeException("Error")).log();

        StepVerifier.create(myMono)
            .expectErrorMessage("Error")
            .verify();
    }

    @Test
    public void coldPublishers() throws InterruptedException {
        Flux<String> myFlux = Flux.just("a", "b", "c", "d", "e", "f")
                .delayElements(Duration.ofSeconds(1));

        // Subscriber 1
        myFlux.subscribe(s -> System.out.println("Element in Subscriber 1 :: "+ s));

        Thread.sleep(2000);

        // Subscriber 2
        myFlux.subscribe(s -> System.out.println("Element in Subscriber 2 :: "+ s));

        Thread.sleep(5000);
    }

    @Test
    public void hotPublishers() throws InterruptedException {
        Flux<String> myFlux = Flux.just("a", "b", "c", "d", "e", "f")
                .delayElements(Duration.ofSeconds(1));

        // Make myflux hot publisher
        ConnectableFlux<String> connectableFlux = myFlux.publish();
        connectableFlux.connect();

        // Subscriber 1
        connectableFlux.subscribe(s -> System.out.println("Element in Subscriber 1 :: "+ s));
        Thread.sleep(2000);

        // Subscriber 2
        connectableFlux.subscribe(s -> System.out.println("Element in Subscriber 2 :: "+ s));

        Thread.sleep(5000);
    }

}
