package com.mynotes.spring.reactive.controller;

import com.mynotes.spring.reactive.dto.CalculateResponseDTO;
import com.mynotes.spring.reactive.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/calculate")
public class CalculateController {

    @Autowired
    CalculateService calculateService;

    @GetMapping("/{input}")
    public Mono<CalculateResponseDTO> calculate(@PathVariable int input) {
        return this.calculateService.squareAndRoot(input);
    }
}
