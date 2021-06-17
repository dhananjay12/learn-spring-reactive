package com.mynotes.reactive.demo.webfluxinservlet.controller.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ApiController {

    @GetMapping("/headers")
    @ResponseBody
    public Map<String, String> headers(@RequestHeader MultiValueMap<String, String> headers) {

        Map<String, String> map = new HashMap<>();

        headers.forEach((key, value) -> {
            log.info(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
            map.put(key, value.stream().collect(Collectors.joining("|")));
        });

        return map;
    }

    @GetMapping("/check1")
    public Mono<String> check1() {

        return Mono.just("check1");
    }

    @GetMapping(value ="/delay" , produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> delay(ServerWebExchange exchange) {

        return Flux.just("delay1","delay2","delay3").delayElements(Duration.ofSeconds(1)).log();
    }

}

