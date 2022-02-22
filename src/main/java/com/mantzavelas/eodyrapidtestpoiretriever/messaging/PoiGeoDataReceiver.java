package com.mantzavelas.eodyrapidtestpoiretriever.messaging;

import com.mantzavelas.eodyrapidtestpoiretriever.rest.GeocodeResponse;
import com.mantzavelas.eodyrapidtestpoiretriever.rest.HereApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class PoiGeoDataReceiver {

    private final HereApiClient hereApiClient;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public PoiGeoDataReceiver(HereApiClient hereApiClient, AmqpTemplate amqpTemplate) {
        this.hereApiClient = hereApiClient;
        this.amqpTemplate = amqpTemplate;
    }

    @RabbitListener(queues = RabbitMQConfiguration.POI_GEODATA_ROUTING_KEY)
    public void poiGeoDataReceiver(byte[] poi) {
        log.debug("Received POI data: " + Arrays.toString(poi));

        PoiGeoData geoData = (PoiGeoData) SerializationUtils.deserialize(poi);
        String title = getLocationWithoutHour(geoData.getPoiTitle());

        hereApiClient.call(title)
            .map(geocodeResponseToPoiGeoData(geoData))
            .subscribe(poiGeoData -> amqpTemplate.convertAndSend(RabbitMQConfiguration.POI_DB_SAVE_QUEUE,
                RabbitMQConfiguration.POI_DB_SAVE_ROUTING_KEY,
                SerializationUtils.serialize(poiGeoData)));
    }

    private Function<GeocodeResponse, PoiGeoData> geocodeResponseToPoiGeoData(PoiGeoData geoData) {
        return response -> {
            response.getItems().stream().findFirst().ifPresent(loc -> {
                geoData.setLatitude(String.valueOf(loc.getPosition().getLat()));
                geoData.setLongitude(String.valueOf(loc.getPosition().getLng()));
            });

            return geoData;
        };
    }

    private String getLocationWithoutHour(String poiData) {
        return Stream.of(poiData.split(","))
            .filter(loc -> !loc.contains(":") && !loc.contains("-"))
            .collect(Collectors.joining(" "));
    }

}
