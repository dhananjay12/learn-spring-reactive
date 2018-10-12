package com.mynotes.spring.reactive.basic;

import java.time.Duration;
import java.util.Arrays;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BasicFluxMono {
	
	Flux<String> createFluxFromValues() {
		return Flux.just("foo", "bar", "foobar");
	}
	
	Flux<String> createFluxFromList() {
		return Flux.fromIterable(Arrays.asList("foo", "bar", "foobar"));
	}
	
	//Flux that emits increasing values from 0 to 9 each 100ms
	Flux<Long> counter() {
		return Flux
				.interval(Duration.ofMillis(100))
				.take(10);  
	}
	
	Mono<String> fooMono() {
		return Mono.just("foo");
	}

}
