package com.mynotes.reactive.demo.webfluxinservlet.controller.controller;

import com.mynotes.reactive.demo.webfluxinservlet.controller.exceptions.RestTemplateResponseErrorHandler;
import com.mynotes.reactive.demo.webfluxinservlet.controller.exceptions.WebClientCustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bin")
public class HttpBinController {

    private final RestTemplate restTemplate;

    private final WebClient webClient;

    private final String baseURL;

    public HttpBinController(RestTemplate restTemplate,
                             @Value("${my.service.url:https://httpbin.org}") String baseURL,
                             RestTemplateResponseErrorHandler handler){
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(handler);
        this.webClient = WebClient.builder().baseUrl(baseURL).build();
        this.baseURL = baseURL;
    }

    @GetMapping("/rest/get")
    public ResponseEntity<?> restTemplateGet(){
       return  this.restTemplate.getForEntity(baseURL+"/get",String.class);
    }

    @GetMapping("/web/get")
    public Mono<String> webClientGet(){
        return this.webClient.get().uri(baseURL+"/get")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        clientResponse -> Mono.error(new WebClientCustomException("Client 4xx error", clientResponse.statusCode())))
                .onStatus(HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(new WebClientCustomException("Client 5xx error", clientResponse.statusCode())))
                .bodyToMono(String.class);
    }


}
