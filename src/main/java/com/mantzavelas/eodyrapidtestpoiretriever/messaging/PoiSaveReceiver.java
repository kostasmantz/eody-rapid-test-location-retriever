package com.mantzavelas.eodyrapidtestpoiretriever.messaging;

import com.mantzavelas.eodyrapidtestpoiretriever.models.TestSiteGeolocation;
import com.mantzavelas.eodyrapidtestpoiretriever.repositories.TestSiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

@Component
@Slf4j
public class PoiSaveReceiver {

    private final TestSiteRepository repository;

    @Autowired
    public PoiSaveReceiver(TestSiteRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitMQConfiguration.POI_DB_SAVE_ROUTING_KEY)
    public void poiGeoDataReceiver(byte[] response) {
        System.out.println("Received POI Geocode response");

        PoiGeoData poiGeoData = (PoiGeoData) SerializationUtils.deserialize(response);
        Mono<TestSiteGeolocation> mono = repository.findByLatitudeAndLongitudeAndEffectiveDateIs(poiGeoData.getLatitude(), poiGeoData.getLongitude(), poiGeoData.getEffectiveDate());

        Mono.just(poiGeoData)
            .map(geoDataToTestSiteMapper())
            .filterWhen(poi -> {
                if (poi.getLatitude() == null || poi.getLongitude() == null) {
                    return Mono.just(true);
                } else {
                    return isNewLocation(mono);
                }
            })
            .subscribe(poiData -> repository.save(poiData).subscribe());
    }

    private Function<PoiGeoData, TestSiteGeolocation> geoDataToTestSiteMapper() {
        return poiGeoData -> {
            TestSiteGeolocation siteGeolocation = new TestSiteGeolocation();
            siteGeolocation.setTitle(poiGeoData.getPoiTitle());
            siteGeolocation.setLatitude(poiGeoData.getLatitude());
            siteGeolocation.setLongitude(poiGeoData.getLongitude());
            siteGeolocation.setCreationDate(Instant.now());
            siteGeolocation.setEffectiveDate(poiGeoData.getEffectiveDate());

            return siteGeolocation;
        };
    }

    private Mono<Boolean> isNewLocation(Mono<TestSiteGeolocation> geolocationMono) {
        return geolocationMono.hasElement().map(b -> !b);
    }

}
