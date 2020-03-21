package com.djcodes.spring.reactivedb.mongo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

interface ReservationRepository extends ReactiveCrudRepository<Reservation,String> {

    Flux<Reservation> findByName(String name);

}
