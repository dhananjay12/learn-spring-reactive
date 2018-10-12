package com.mynotes.spring.reactive.reactivewebapp.steps;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.mynotes.spring.reactive.reactivewebapp.Person;

public class Step1 {

	private static final Logger logger = LoggerFactory.getLogger(Step1.class);

	private static RestTemplate restTemplate = new RestTemplate();

	static {
		String baseUrl = "http://localhost:8081?delay=2";
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
	}

	public static void main(String[] args) {

		Instant start = Instant.now();

		for (int i = 1; i <= 3; i++) {
			restTemplate.getForObject("/person/{id}", Person.class, i);
		}

		logTime(start);
	}

	private static void logTime(Instant start) {
		logger.debug("Elapsed time: " + Duration.between(start, Instant.now()).toMillis() + "ms");
	}

}
