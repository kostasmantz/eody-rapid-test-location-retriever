package com.mantzavelas.eodyrapidtestpoiretriever.controllers;

import com.mantzavelas.eodyrapidtestpoiretriever.models.TestSiteGeolocation;
import com.mantzavelas.eodyrapidtestpoiretriever.services.RapidTestPoiService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final RapidTestPoiService service;

    @Autowired
    public PlaceController(RapidTestPoiService service) {
        this.service = service;
    }

    @GetMapping
    public Publisher<TestSiteGeolocation> getLocations(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.getPlaces(from.toInstant(ZoneOffset.UTC), to.toInstant(ZoneOffset.UTC));
    }
}
