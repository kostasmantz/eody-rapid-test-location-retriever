package com.mantzavelas.eodyrapidtestpoiretriever.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeocodeResponse implements Serializable {

    private static final long serialVersionUID = -5745865005583011385L;

    private List<Geolocation> items;
}
