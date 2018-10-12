package com.mynotes.spring.reactive.basic;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BasicFluxMonoTest {
	
	BasicFluxMono basicFluxMono=new BasicFluxMono();
	
	@Test
	public void fromValues() {
		Flux<String> flux = basicFluxMono.createFluxFromValues();
		StepVerifier.create(flux)
				.expectNext("foo", "bar", "foobar")
				.verifyComplete();
	}

//========================================================================================

	@Test
	public void fromList() {
		Flux<String> flux = basicFluxMono.createFluxFromList();
		StepVerifier.create(flux)
		.expectNext("foo", "bar", "foobar")
				.verifyComplete();
	}

}
