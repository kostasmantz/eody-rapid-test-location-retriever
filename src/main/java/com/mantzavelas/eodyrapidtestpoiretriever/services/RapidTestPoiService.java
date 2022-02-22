package com.mantzavelas.eodyrapidtestpoiretriever.services;

import com.mantzavelas.eodyrapidtestpoiretriever.models.TestSiteGeolocation;
import com.mantzavelas.eodyrapidtestpoiretriever.repositories.TestSiteRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RapidTestPoiService {

    private final TestSiteRepository repository;

    @Autowired
    public RapidTestPoiService(TestSiteRepository repository) {
        this.repository = repository;
    }

    public Publisher<TestSiteGeolocation> getPlaces(Instant from, Instant to) {
        return repository.findAllByEffectiveDateBetween(from, to);
    }
}
