package com.mynotes.spring.reactive.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/flux")
    public Flux<Integer> flux(){
        return Flux.just(1,2,3,4)
                .concatWith(Mono.error(new RuntimeException("Runtime Exception")))
                .log();
    }

    @GetMapping("/flux/{limit}")
    public Flux<Integer> fluxLimit(@PathVariable int limit){

        if(limit < 1){
            return Flux.error(new IllegalArgumentException("Limit cannot be less than 1."));
        }

        return Flux.range(1,limit)
                .log();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception){
        System.out.println("Exception :: "+ exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
