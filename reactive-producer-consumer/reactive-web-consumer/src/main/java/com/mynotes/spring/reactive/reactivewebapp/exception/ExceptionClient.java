package com.mynotes.spring.reactive.reactivewebapp.exception;

import com.mynotes.spring.reactive.reactivewebapp.Person;
import com.mynotes.spring.reactive.reactivewebapp.steps.Step2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ExceptionClient {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionClient.class);

    private static WebClient client = WebClient.create("http://localhost:8080");

    public static void main(String[] args) throws InterruptedException {

        client.get().uri("/exception/flux")
        .retrieve()
        .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
            Mono<String> errorMono = clientResponse.bodyToMono(String.class);
            return errorMono.flatMap(errMsg -> {
                logger.debug("Error Message is "+ errMsg);
                return Mono.error(new RuntimeException(errMsg));
            });
        })
        ;


        client.get().uri("/exception/flux")
                .exchange()
                .flatMapMany(clientResponse -> {
                    if(clientResponse.statusCode().is5xxServerError()){
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errMsg -> {
                                    logger.error("Error Message is "+ errMsg);
                                    throw new RuntimeException(errMsg);
                                });
                    }else{
                        return clientResponse.bodyToFlux(Integer.class);
                    }
                });


        Thread.sleep(5000);
    }
}
