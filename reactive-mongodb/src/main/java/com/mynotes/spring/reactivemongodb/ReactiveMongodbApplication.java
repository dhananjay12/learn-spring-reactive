package com.mynotes.spring.reactivemongodb;

import java.util.function.Consumer;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveMongodbApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ReactiveMongodbApplication.class, args);
		Thread.sleep(10000);
	}
}

@Log
@Component
class DataInitializer implements ApplicationRunner {

	private final ReservationRepo reservationRepo;

	DataInitializer(ReservationRepo reservationRepo) {
		this.reservationRepo = reservationRepo;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		this.reservationRepo.deleteAll()
			.thenMany(Flux.just("Jane", "John", "Max", "Josh", "Aloy"))
				.map(name -> new Reservation(null, name)).flatMap(this.reservationRepo::save)
				.thenMany(this.reservationRepo.findAll()).subscribe(System.out::println);

	}

}

interface ReservationRepo extends ReactiveMongoRepository<Reservation, String> {

}

@Data
@AllArgsConstructor
@Document
class Reservation {

	@Id
	private String id;

	private String reservationName;
}
