package com.example.gateway.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final Set<HttpMethod> CSRF_METHOD_TO_CHECK =
            new HashSet<>(Arrays.asList(POST, PUT, PATCH, DELETE));

    public static String AUTHORIZATION_COOKIE_NAME = "Authorization";

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                .authorizeExchange()
                .anyExchange()
                .permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                .requireCsrfProtectionMatcher(
                        exchange -> {
                            ServerHttpRequest request = exchange.getRequest();
                            if(CSRF_METHOD_TO_CHECK.contains(request.getMethod())
                             && request.getCookies().containsKey(AUTHORIZATION_COOKIE_NAME)){
                                return match();
                            } else {
                                return notMatch();
                            }
                        }
                )
                .and()
                .logout().disable().build();

    }


    //Reactive fix - https://github.com/spring-projects/spring-security/issues/5766#issuecomment-564636167
    @Bean
    WebFilter addCsrfToken() {
        return (exchange, next) -> exchange
                .<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName())
                .doOnSuccess(token -> {}) // do nothing, just subscribe :/
                .then(next.filter(exchange));
    }
}
