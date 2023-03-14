package br.pucpr.petapi.lib.location;

import br.pucpr.petapi.lib.location.dto.response.CPFDataResponse;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;

@Component
public class LocationUtils {
    private final LocationSettings settings;
    private final String PLACEHOLDER_CEP = "00000000";

    public LocationUtils(LocationSettings settings) {
        this.settings = settings;
    }

    public CPFDataResponse getCPFData(String cpf){
        var template = new RestTemplate();
        return template.exchange(getCPFRequestUrl(cpf),
                GET,
                HttpEntity.EMPTY,
                CPFDataResponse.class).getBody();
    }

    private String getCPFRequestUrl(String cpf){
        return settings.getCepApiUrl().replace(PLACEHOLDER_CEP, cpf);
    }
}
