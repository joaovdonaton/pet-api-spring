package br.pucpr.petapi.lib.location;

import br.pucpr.petapi.lib.location.dto.response.CEPDataResponse;
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

    public CEPDataResponse getCEPData(String cpf){
        var template = new RestTemplate();
        return template.exchange(getCEPRequestUrl(cpf),
                GET,
                HttpEntity.EMPTY,
                CEPDataResponse.class).getBody();
    }

    private String getCEPRequestUrl(String cpf){
        return settings.getCepApiUrl().replace(PLACEHOLDER_CEP, cpf);
    }
}
