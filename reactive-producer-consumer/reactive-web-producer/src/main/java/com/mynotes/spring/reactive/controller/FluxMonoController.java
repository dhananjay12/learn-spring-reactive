package com.mynotes.spring.reactive.controller;

import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxMonoController {

    @GetMapping("/flux")
    public Flux<Integer> flux(){
        return Flux.just(1,2,3,4)
            .delayElements(Duration.ofSeconds(1))
            .log();
    }

    @GetMapping(value = "/flux-stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxStream(){
        return Flux.just(1,2,3,4)
            .delayElements(Duration.ofSeconds(1))
            .log();
    }

    @GetMapping(value = "/flux-stream-infinite", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> fluxStreamInfinite(){
        return Flux.interval(Duration.ofSeconds(1))
            .log();
    }

    @GetMapping("/mono")
    public Mono<Integer> mono(){
        return Mono.just(1)
            .log();
    }

}
