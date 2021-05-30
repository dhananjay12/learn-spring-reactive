package com.djcodes.spring.reactivedb;

import com.djcodes.spring.reactivedb.document.Reservation;
import com.djcodes.spring.reactivedb.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class ReactiveDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveDbApplication.class, args);
    }

}

@Component
@Profile("dev")
class DataInitializer implements CommandLineRunner {

    @Autowired
    ReservationRepository reservationRepository;

    List<Reservation> sampleData = List.of(new Reservation(null, "John", LocalDate.of(2020, 04, 05)),
            new Reservation(null, "Jane", LocalDate.of(2020, 04, 12)),
            new Reservation(null, "Max", LocalDate.of(2020, 05, 23)),
            new Reservation("abc123", "Paul", LocalDate.of(2020, 05, 12)));

    @Override
    public void run(String... args) throws Exception {
        reservationRepository.deleteAll()
                .thenMany(Flux.fromIterable(sampleData))
                .flatMap(reservationRepository::save)
                .doOnNext(item -> System.out.println("inserted Item" + item));
    }
}
