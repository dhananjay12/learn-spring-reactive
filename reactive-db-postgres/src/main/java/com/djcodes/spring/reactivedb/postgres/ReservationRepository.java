package com.djcodes.spring.reactivedb.postgres;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

interface ReservationRepository extends ReactiveCrudRepository<Reservation, Integer> {

}
