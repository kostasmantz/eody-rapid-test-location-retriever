package com.mantzavelas.eodyrapidtestpoiretriever.repositories;

import com.mantzavelas.eodyrapidtestpoiretriever.models.TestSiteGeolocation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public interface TestSiteRepository extends ReactiveMongoRepository<TestSiteGeolocation, String> {

    Mono<TestSiteGeolocation> findByLatitudeAndLongitudeAndEffectiveDateIs(String latitude, String longitude, Instant effectiveDate);

    Flux<TestSiteGeolocation> findAllByEffectiveDateBetween(Instant from, Instant to);
}
