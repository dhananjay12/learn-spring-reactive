package com.djcodes.spring.reactivedb.controller;

import com.djcodes.spring.reactivedb.document.Reservation;
import com.djcodes.spring.reactivedb.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @GetMapping
    public Flux<Reservation> getAll(){
        return reservationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Reservation>> findById(@PathVariable String id){
        return reservationRepository.findById(id)
                .map(element -> new ResponseEntity<>(element,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Reservation> create(@RequestBody Reservation reservation){
        return reservationRepository.save(reservation);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return reservationRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Reservation>> update(@PathVariable String id, @RequestBody Reservation reservation){
        return reservationRepository.findById(id)
                .flatMap(element -> {
                    element.setName(reservation.getName());
                    element.setDate(reservation.getDate());
                    return  reservationRepository.save(element);
                }).map(updatedElement -> new ResponseEntity<>(updatedElement,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
