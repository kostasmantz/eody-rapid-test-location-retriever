package com.mantzavelas.eodyrapidtestpoiretriever.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeolocationPosition implements Serializable {

    private static final long serialVersionUID = -409585973998382798L;

    private Double lat;
    private Double lng;
}
