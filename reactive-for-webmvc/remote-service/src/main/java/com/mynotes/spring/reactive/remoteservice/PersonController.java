package com.mynotes.spring.reactive.remoteservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

	private static List<Person> list = new ArrayList<>();

	static {
		list.add(new Person(1, "John"));
		list.add(new Person(2, "Jane"));
		list.add(new Person(3, "Max"));
		list.add(new Person(4, "Alex"));
		list.add(new Person(5, "Aloy"));
		list.add(new Person(6, "Sarah"));
	}

	@GetMapping("/person/{id}")
	public Person getPerson(@PathVariable int id, @RequestParam int delay) throws InterruptedException {
		Thread.sleep(delay * 1000);
		return list.stream().filter((person) -> person.getId() == id).findFirst().get();
	}

}
