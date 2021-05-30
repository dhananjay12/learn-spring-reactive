package com.djcodes.spring.reactivedb.controller;

import com.djcodes.spring.reactivedb.document.Reservation;
import com.djcodes.spring.reactivedb.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class ReservationControllerTest {

    @Autowired
    WebTestClient webTestClient;

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
    public void getAll() {
        webTestClient.get().uri("/reservations")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Reservation.class)
                .hasSize(4);
    }

    @Test
    public void getSingle_success() {
        webTestClient.get().uri("/reservations/abc123")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Reservation.class);
    }

    @Test
    public void getSingle_not_found() {
        webTestClient.get().uri("/reservations/asd")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void save() {
        Reservation request = new Reservation(null, "Emma", LocalDate.of(2021,04,11));
        webTestClient.post().uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), Reservation.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Emma");
    }

    @Test
    public void delete() {
        webTestClient.delete().uri("/reservations/abc123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void update() {
        Reservation request = new Reservation(null, "Paul", LocalDate.of(2021, 11, 12));
        webTestClient.put().uri("/reservations/abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), Reservation.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Paul")
                .jsonPath("$.date").isEqualTo("2021-11-12");
    }

    @Test
    public void update_notfound() {
        Reservation request = new Reservation(null, "Paul", LocalDate.of(2021, 11, 12));
        webTestClient.put().uri("/reservations/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), Reservation.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}