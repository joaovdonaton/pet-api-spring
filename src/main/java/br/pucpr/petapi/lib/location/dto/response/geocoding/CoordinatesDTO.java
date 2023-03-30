package br.pucpr.petapi.lib.location.dto.response.geocoding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesDTO {
    private String lat;
    private String lng;
}
