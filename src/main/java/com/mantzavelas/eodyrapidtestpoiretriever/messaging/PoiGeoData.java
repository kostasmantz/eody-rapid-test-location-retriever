package com.mantzavelas.eodyrapidtestpoiretriever.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoiGeoData implements Serializable {

    private static final long serialVersionUID = 3245107884987377340L;

    private String poiTitle;
    private Instant effectiveDate;
    private String latitude;
    private String longitude;
}
