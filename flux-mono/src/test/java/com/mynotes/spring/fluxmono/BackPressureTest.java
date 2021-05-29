package com.mynotes.spring.fluxmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTest {

    @Test
    public void backPressureTest(){
        Flux<Integer> flux = Flux.range(1,10).log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenRequest(1)
                .expectNext(3)
                .thenCancel()
                .verify();

    }

    @Test
    public void backPressureTest_subscribe(){
        Flux<Integer> flux = Flux.range(1,10).log();

        flux.subscribe(
                element -> System.out.println("Element :: "+element),
                exception -> System.out.println("Error"),
                () -> System.out.println("Complete"),
                subscription -> subscription.request(2));
    }

    @Test
    public void backPressureTest_custom(){
        Flux<Integer> flux = Flux.range(1,10).log();

        flux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Value received = "+ value);
                if(value==4){
                    cancel();
                }
            }
        });
    }
}
