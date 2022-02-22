package com.mantzavelas.eodyrapidtestpoiretriever.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("test-site-geolocations")
public class TestSiteGeolocation {

    @Id
    private String id;
    private String title;
    private String latitude;
    private String longitude;
    private Instant creationDate;
    private Instant effectiveDate;
}
