package br.pucpr.petapi.lib.location.dto.response.geocoding;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoordinatesDTO {
    private String lat;
    private String lng;
}
