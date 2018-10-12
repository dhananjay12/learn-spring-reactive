package com.example.reactive.reactiveweb;

import java.time.Duration;
import java.time.Instant;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class GreetingsRestController {

	@GetMapping(value = "/greeting/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Publisher<Greeting> sseGreetings() {
		Flux<Greeting> delayElements = Flux
				.<Greeting>generate(sink -> sink.next(new Greeting("Hello @" + Instant.now().toString())))
				.delayElements(Duration.ofSeconds(1));
		return delayElements;
	}

	@GetMapping("/greeting")
	Publisher<Greeting> greetingPublisher() {
		Flux<Greeting> greetingFlux = Flux.<Greeting>generate(sink -> sink.next(new Greeting("Hello"))).take(100);
		return greetingFlux;
	}

}
