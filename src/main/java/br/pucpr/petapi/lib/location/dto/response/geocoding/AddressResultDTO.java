package br.pucpr.petapi.lib.location.dto.response.geocoding;

import lombok.Data;

@Data
public class AddressResultDTO {
    private GeometryDTO geometry;
}
