package com.mantzavelas.eodyrapidtestpoiretriever.scrape;

import com.mantzavelas.eodyrapidtestpoiretriever.messaging.PoiGeoData;
import com.mantzavelas.eodyrapidtestpoiretriever.messaging.RabbitMQConfiguration;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Calendar;

@Component
public class EodySiteParser {

    private final AmqpTemplate amqpTemplate;
    private final WebClient webClient;

    @Autowired
    public EodySiteParser(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
        this.webClient = WebClient.builder()
            .baseUrl("https://eody.gov.gr")
            .clientConnector(new ReactorClientHttpConnector(HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)))
            .build();
    }

    public Mono<Void> parse() {
        Mono<String> response = webClient
            .get()
            .uri("/komy-testing-eody/")
            .retrieve()
            .bodyToMono(String.class);

        response.subscribe(html -> {
            Element bodyElement = Jsoup.parse(html).body();
            Elements olElements = bodyElement.getElementsByTag("ol");
            Element header = bodyElement.getElementById("main-inner-heading");
            String headerDate = header.wholeText();
            Instant date = Arrays.stream(headerDate.split(" "))
                .filter(s -> s.contains("(") && s.contains(")"))
                .findFirst()
                .map(s -> s.replace("(", "").replace(")", ""))
                .map(s -> LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR),
                    Integer.parseInt(s.split("/")[1]),
                    Integer.parseInt(s.split("/")[0]), 1, 0))
                .map(dateTime -> dateTime.toInstant(ZoneOffset.UTC))
                .orElse(null);

            olElements.stream()
                .findFirst()
                .map(el -> el.getElementsByTag("li"))
                .ifPresent(el -> el.forEach(liElement -> {
                    amqpTemplate.convertAndSend(RabbitMQConfiguration.POI_GEODATA_QUEUE,
                        RabbitMQConfiguration.POI_GEODATA_ROUTING_KEY,
                        SerializationUtils.serialize(PoiGeoData.builder().poiTitle(liElement.wholeText()).effectiveDate(date).build()));
                }));
        });

        return Mono.empty();
    }
}
