package com.mynotes.spring.reactive.controller;

import com.mynotes.spring.reactive.dto.CalculateResponseDTO;
import com.mynotes.spring.reactive.service.HttpBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/httpbin")
public class HttpBinController {

    @Autowired
    HttpBinService httpBinService;

    @GetMapping("/call")
    public Mono<String> anything() {
        return this.httpBinService.binCall();
    }
}
