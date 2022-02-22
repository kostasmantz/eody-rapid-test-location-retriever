package com.mantzavelas.eodyrapidtestpoiretriever.rest;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HereApiClient {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final WebClient webClient;
    private final RateLimiter rateLimiter;

    public HereApiClient() {
        HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
        this.webClient = WebClient.builder()
            .baseUrl("https://geocode.search.hereapi.com/v1/")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        this.rateLimiter = RateLimiter.of("my-rate-limiter",
            RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .timeoutDuration(Duration.ofMinutes(10))
                .build());
    }

    public Mono<GeocodeResponse> call(String query) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("/geocode")
                .queryParam("q", query)
                .queryParam("apiKey", "iwtl9OhJ9d6X9xcoNnLPjc1XGBKcuJ4Eo-3ftgoxDQQ").build())
            .retrieve()
            .bodyToMono(GeocodeResponse.class)
            .doOnSubscribe(s -> System.out.println(COUNTER.incrementAndGet() + " - " + LocalDateTime.now()
                + " - call triggered"))
            .transformDeferred(RateLimiterOperator.of(rateLimiter));
    }

}
