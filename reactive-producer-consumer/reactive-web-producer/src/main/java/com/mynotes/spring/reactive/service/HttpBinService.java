package com.mynotes.spring.reactive.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@Slf4j
public class HttpBinService {;

    private final WebClient webClient;

    public HttpBinService(@Value("${my.service.url:}") String baseURL){
        this.webClient = WebClient.builder().baseUrl(baseURL)
                .filter(logRequest())
                .filter(logResposneStatus())
                .defaultHeader("headerKey","headerValue").build();
    }

    public Mono<String> binCall() {
        return this.webClient.get().uri("/anything")
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND),
                        clientResponse -> Mono.error(new RuntimeException("not_found")))
                .bodyToMono(String.class).onErrorReturn("error");
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: " + clientRequest.method()+ " " + clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> {
                        log.info(String.format("Header '%s' = %s", name, values.stream().collect(Collectors.joining(","))));
                    });
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResposneStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

}
