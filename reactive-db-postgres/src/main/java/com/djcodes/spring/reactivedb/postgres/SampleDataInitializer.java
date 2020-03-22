package com.djcodes.spring.reactivedb.postgres;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
@Log4j2
public class SampleDataInitializer {

    private final ReservationService reservationService;

    private final ReservationRepository reservationRepository;


    @EventListener(ApplicationReadyEvent.class)
    public void ready() {

// Wrong name condition
//        Flux<Reservation> reservationFlux = reservationService
//            .saveAll("Jane", "John", "Max", "Josh", "Jo");

        Flux<Reservation> reservationFlux = reservationService
            .saveAll("Jane", "John", "Max", "Josh", "Aloy");

        this.reservationRepository
            .deleteAll()
            .thenMany(reservationFlux)
            .thenMany(this.reservationRepository.findAll())
            .subscribe(log::info);


    }

}
