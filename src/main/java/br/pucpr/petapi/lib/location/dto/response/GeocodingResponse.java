package br.pucpr.petapi.lib.location.dto.response;

import br.pucpr.petapi.lib.location.dto.response.geocoding.AddressResultDTO;
import lombok.Data;

import java.util.Set;

@Data
public class GeocodingResponse {
    private Set<AddressResultDTO> results;
}
