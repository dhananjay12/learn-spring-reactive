package com.example.reactive.reactiveweb;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class WebSocketConfiguration {

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	@Bean
	WebSocketHandler webSocketHandler() {
		return new WebSocketHandler() {

			@Override
			public Mono<Void> handle(WebSocketSession session) {

				Flux<WebSocketMessage> generate = Flux
						.<Greeting>generate(sink -> sink.next(new Greeting("Hello @" + Instant.now().toString())))
						.map(g -> session.textMessage(g.getText())).delayElements(Duration.ofSeconds(1))
						.doFinally(signalType -> System.out.println("Goodbye"));
				return session.send(generate);
			}
		};
	}
	
	@Bean
	HandlerMapping handlerMapping() {
		SimpleUrlHandlerMapping simpleUrlHandlerMapping=new SimpleUrlHandlerMapping();
		simpleUrlHandlerMapping.setUrlMap(Collections.singletonMap("/ws/hello",webSocketHandler()));
		simpleUrlHandlerMapping.setOrder(10);
		
		return simpleUrlHandlerMapping;
	}

}
