package com.djcodes.spring.reactivedb.repository;

import com.djcodes.spring.reactivedb.document.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    List<Reservation> sampleData = List.of(new Reservation(null, "John", LocalDate.of(2020, 04, 05)),
            new Reservation(null, "Jane", LocalDate.of(2020, 04, 12)),
            new Reservation(null, "Max", LocalDate.of(2020, 05, 23)),
            new Reservation("abc123", "Paul", LocalDate.of(2020, 05, 12)));

    @BeforeEach
    public void setup() {
        reservationRepository.deleteAll()
                .thenMany(Flux.fromIterable(sampleData))
                .flatMap(reservationRepository::save)
                .doOnNext(item -> System.out.println("inserted Item" + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(reservationRepository.findAll())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void findByName() {
        StepVerifier.create(reservationRepository.findByName("John"))
                .expectSubscription()
                .expectNextMatches(
                        item -> item.getName().equals("John")
                )
                .verifyComplete();
    }

    @Test
    public void saveReservation() {

        Reservation reservation = new Reservation(null, "Emma", LocalDate.of(2020, 04, 12));

        Mono<Reservation> savedReservation = reservationRepository.save(reservation);

        StepVerifier.create(savedReservation)
                .expectSubscription()
                .expectNextMatches(
                        item -> item.getId() != null && item.getName().equals("Emma")
                )
                .verifyComplete();
    }

    @Test
    public void updateReservation() {


        Flux<Reservation> updateFlux = reservationRepository.findByName("John")
                .map(reservation -> {
                    reservation.setDate(LocalDate.of(2020, 06, 12));
                    return reservation;
                })
                .flatMap(reservation ->  reservationRepository.save(reservation));

        StepVerifier.create(updateFlux)
                .expectSubscription()
                .expectNextMatches(
                        item -> item.getDate().equals(LocalDate.of(2020, 06, 12))
                )
                .verifyComplete();
    }

    @Test
    public void deleteReservation() {


        Mono<Void> deleteReservation = reservationRepository.findByName("John")
                .flatMap(reservation -> reservationRepository.delete(reservation)).then();

        StepVerifier.create(deleteReservation)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(reservationRepository.findAll())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }
}
