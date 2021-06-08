package com.mynotes.spring.reactive.service;

import com.mynotes.spring.reactive.dto.CalculateResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CalculateService {

    //Request and response
    public Mono<CalculateResponseDTO> squareAndRoot(int input) {
        return Mono.just(input)
                .map(this::calculate);
    }

    public CalculateResponseDTO calculate(int input) {
        return new CalculateResponseDTO(input, Math.multiplyExact(input, input), Math.sqrt(input));
    }

}
