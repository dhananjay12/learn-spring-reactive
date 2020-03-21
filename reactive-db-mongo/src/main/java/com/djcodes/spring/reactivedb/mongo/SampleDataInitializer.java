package com.djcodes.spring.reactivedb.mongo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
@Log4j2
public class SampleDataInitializer {

    private final ReactiveCrudRepository reactiveCrudRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        Flux<Reservation> reservations = Flux.just("Jane", "John", "Max", "Josh", "Aloy")
            .map(name -> new Reservation(null, name))
            .flatMap(r -> this.reactiveCrudRepository.save(r));

        this.reactiveCrudRepository
            .deleteAll()
            .thenMany(reservations)
            .thenMany(this.reactiveCrudRepository.findAll())
            .subscribe(log::info);
    }

}
