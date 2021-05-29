package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Locale;

import static reactor.core.scheduler.Schedulers.parallel;

public class MapTest {

    @Test
    public void usingMap(){
        List<String> myList = List.of("foo","bar","baz");

        Flux<String> flux = Flux.fromIterable(myList)
                .map(s -> s.toUpperCase(Locale.ROOT))
                .log();

        StepVerifier.create(flux)
                .expectNext("FOO","BAR","BAZ")
                .verifyComplete();
    }

    @Test
    public void usingMap_datatype(){
        List<String> myList = List.of("foo","bar","baz");

        Flux<Integer> flux = Flux.fromIterable(myList)
                .map(s -> s.length())
                .log();

        StepVerifier.create(flux)
                .expectNext(3,3,3)
                .verifyComplete();
    }

    @Test
    public void flatMap(){
        List<String> myList = List.of("one","two","three", "four", "five","six");
        TestUtils.startTimer();
        Flux<String> flux = Flux.fromIterable(myList)
                .flatMap(s -> Flux.just(convertToUpperCase(s)))
                .log();

        StepVerifier.create(flux)
                .expectNext("ONE","TWO","THREE","FOUR", "FIVE","SIX")
                .verifyComplete();
        TestUtils.stopTimer();
    }

    private String convertToUpperCase(String input){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input.toUpperCase(Locale.ROOT);
    }

    @Test
    public void flatMap_parallel(){
        List<String> myList = List.of("one","two","three", "four", "five","six");
        TestUtils.startTimer();
        Flux<String> flux = Flux.fromIterable(myList)
                .window(2)
                .flatMap(s -> s.map(this::convertToUpperCase).subscribeOn(parallel()))
                .log();

        StepVerifier.create(flux)
                .expectNextCount(6) //Not same order
                .verifyComplete();
        TestUtils.stopTimer();
    }

    @Test
    public void flatMap_parallel_sequential(){
        List<String> myList = List.of("one","two","three", "four", "five","six");
        TestUtils.startTimer();
        Flux<String> flux = Flux.fromIterable(myList)
                .window(2)
                .flatMapSequential(s -> s.map(this::convertToUpperCase).subscribeOn(parallel()))
                .log();

        StepVerifier.create(flux)
                .expectNext("ONE","TWO","THREE","FOUR", "FIVE","SIX")
                .verifyComplete();
        TestUtils.stopTimer();
    }





}
