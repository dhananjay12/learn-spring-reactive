package com.mynotes.spring.reactive.remoteservice;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.web.reactive.function.BodyInserters.*;
import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;



import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PersonController {

	private static List<Person> personList = new ArrayList<>();
	
	private static List<Hobby> hobbyList = new ArrayList<>();

	static {
		personList.add(new Person(1, "John"));
		personList.add(new Person(2, "Jane"));
		personList.add(new Person(3, "Max"));
		personList.add(new Person(4, "Alex"));
		personList.add(new Person(5, "Aloy"));
		personList.add(new Person(6, "Sarah"));
		
		hobbyList.add(new Hobby(1, "Travel"));
		hobbyList.add(new Hobby(2, "Gaming"));
		hobbyList.add(new Hobby(3, "Puzzles"));
		hobbyList.add(new Hobby(4, "Reading"));
		hobbyList.add(new Hobby(5, "Cooking"));
		hobbyList.add(new Hobby(6, "Running"));
		
	}

	@GetMapping("/person/{id}")
	public Person getPerson(@PathVariable int id, @RequestParam(defaultValue="2") int delay) throws InterruptedException {
		Thread.sleep(delay * 1000);
		return personList.stream().filter((person) -> person.getId() == id).findFirst().get();
	}
	
	@GetMapping("/person/{id}/hobby")
	public Hobby getPersonHobby(@PathVariable int id, @RequestParam(defaultValue="2") int delay) throws InterruptedException {
		Thread.sleep(delay * 1000);
		return hobbyList.stream().filter((person) -> person.getPersonId() == id).findFirst().get();
	}
	
	/*@GetMapping("/persons/events1")
	public ResponseBodyEmitter getPersonsPublisher(@RequestParam(defaultValue="1") int delay)  {
		
		Flux<Person> delayElements = Flux.interval(Duration.ofSeconds(delay))
				.map(i -> personList.get(i.intValue()));
				
		return delayElements;
	}*/
	
	@GetMapping("/persons/events2")
	public Publisher<?> getPersons(@RequestParam(defaultValue="1") int delay)  {
		
		Flux<ServerSentEvent<Person>> stream =
				Flux.interval(Duration.ofSeconds(delay))
						.map(i -> personList.get(i.intValue()))
						.map(data -> ServerSentEvent.builder(data).build());
		return ServerResponse.ok().body(fromServerSentEvents(stream));
	
	}
	
	

}
