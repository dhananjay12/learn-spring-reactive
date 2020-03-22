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

    private final ReservationRepository reservationRepository;

    private final DatabaseClient databaseClient;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        Flux<Reservation> reservations = Flux.just("Jane", "John", "Max", "Josh", "Aloy")
                    .map(name -> new Reservation(null, name))
                  .flatMap(r -> this.reservationRepository.save(r));

        this.reservationRepository
            .deleteAll()
            .thenMany(reservations)
            .thenMany(this.reservationRepository.findAll())
            .subscribe(log::info);



        this.databaseClient
            .select()
            .from("reservation").as(Reservation.class)
            .fetch()
            .all()
            .subscribe(log::warn);
    }

}
