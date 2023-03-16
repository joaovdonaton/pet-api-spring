package br.pucpr.petapi.lib.location.dto.response;

import br.pucpr.petapi.lib.location.dto.response.geocoding.AddressResultDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class GeocodingResponse {
    public static final String NO_RESULTS_STATUS = "ZERO_RESULTS";
    public static final String OK_STATUS = "OK";
    private List<AddressResultDTO> results;
    private String status;
}
