package com.djcodes.spring.reactivedb.repository;

import com.djcodes.spring.reactivedb.document.Reservation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


public interface ReservationRepository extends ReactiveCrudRepository<Reservation,String> {

    Flux<Reservation> findByName(String name);

}
