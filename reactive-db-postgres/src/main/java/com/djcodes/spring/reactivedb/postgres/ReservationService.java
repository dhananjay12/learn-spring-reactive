package com.djcodes.spring.reactivedb.postgres;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

@Service
@Transactional
@RequiredArgsConstructor
class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TransactionalOperator transactionalOperator;

    public Flux<Reservation> saveAll(String... names) {
        Flux<Reservation> reservations = Flux.fromArray(names)
            .map(name -> new Reservation(null, name))
            .flatMap(r -> this.reservationRepository.save(r))
            .doOnNext(this::assertValid);
        return reservations;
        //explicitely telling if not using the annotation - @Transactional - @EnableTransactionManagement
        //return this.transactionalOperator.transactional(reservations);
    }

    private void assertValid(Reservation r) {
        Assert.isTrue(r.getName() != null
            && r.getName().length() > 2, "the name must be more than 2 letters");
    }
}
