package com.mynotes.spring.reactive.reactivewebapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class ReactiveWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWebAppApplication.class, args);
	}

	// Below wont work with new spring boot > 2.3.2
//	@Bean
//	public CommonsRequestLoggingFilter requestLoggingFilter() {
//	    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
//	    loggingFilter.setIncludeClientInfo(true);
//	    loggingFilter.setIncludeQueryString(true);
//	    loggingFilter.setIncludePayload(true);
//	    loggingFilter.setIncludeHeaders(false);
//	    return loggingFilter;
//	}
}


