package com.mynotes.spring.reactive.controller;

import java.time.Duration;
import java.time.Instant;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class GreetReactiveController {	

	@GetMapping("/greetings")
	Publisher<Greeting> greetingPublisher() {
		Flux<Greeting> greetingFlux = Flux.<Greeting>generate(sink -> sink.next(new Greeting("Hello"))).take(50);
		return greetingFlux;
	}
	
	@GetMapping(value = "/greetings/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Publisher<String> sseGreetings() {
		Flux<String> delayElements = Flux
				.<String>generate(sink -> sink.next(new String("Hello @" + Instant.now().toString())))
				.delayElements(Duration.ofSeconds(1));
		return delayElements;
	}


}
