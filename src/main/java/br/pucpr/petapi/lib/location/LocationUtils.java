package br.pucpr.petapi.lib.location;

import br.pucpr.petapi.lib.location.dto.response.CEPDataResponse;
import br.pucpr.petapi.lib.location.dto.response.GeocodingResponse;
import br.pucpr.petapi.lib.security.ApiKeysSettings;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;

@Component
public class LocationUtils {
    private final LocationSettings locationSettings;
    private final ApiKeysSettings apiKeysSettings;
    private final String PLACEHOLDER_CEP = "00000000";
    private final String PLACEHOLDER_ADDRESS = "ADDRESS";
    private final String PLACEHOLDER_KEY = "KEY";

    public LocationUtils(LocationSettings locationSettings, ApiKeysSettings apiKeysSettings) {
        this.locationSettings = locationSettings;
        this.apiKeysSettings = apiKeysSettings;
    }

    public CEPDataResponse getCEPData(String cpf){
        var template = new RestTemplate();
        return template.exchange(getCEPRequestUrl(cpf),
                GET,
                HttpEntity.EMPTY,
                CEPDataResponse.class).getBody();
    }

    public void getCoordinates(String address){
        var template = new RestTemplate();
        System.out.println(getCoordinatesRequestUrl(address));
        var a = template.exchange(getCoordinatesRequestUrl(address),
                GET,
                HttpEntity.EMPTY,
                GeocodingResponse.class);

        a.getBody().getResults().stream().forEach(v -> {
            var b = v.getGeometry().getLocation();
            System.out.println(b.getLat());
            System.out.println(b.getLng());
        });
    }

    private String getCEPRequestUrl(String cpf){
        return locationSettings.getCepApiUrl().replace(PLACEHOLDER_CEP, cpf);
    }
    private String getCoordinatesRequestUrl(String address){
        return locationSettings.getGoogleGoecodingUrl()
                .replace(PLACEHOLDER_ADDRESS, address)
                .replace(PLACEHOLDER_KEY, apiKeysSettings.getGoogleApi());
    }
}
