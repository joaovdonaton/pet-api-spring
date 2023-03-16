package br.pucpr.petapi.lib.location;

import br.pucpr.petapi.lib.location.dto.response.CEPDataResponse;
import br.pucpr.petapi.lib.location.dto.response.GeocodingResponse;
import br.pucpr.petapi.lib.location.dto.response.geocoding.CoordinatesDTO;
import br.pucpr.petapi.lib.security.ApiKeysSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(LocationUtils.class);

    public LocationUtils(LocationSettings locationSettings, ApiKeysSettings apiKeysSettings) {
        this.locationSettings = locationSettings;
        this.apiKeysSettings = apiKeysSettings;
    }

    public CEPDataResponse getCEPData(String cpf){
        String url = getCEPRequestUrl(cpf);
        logger.info("Fetching CEP data at: " + url);
        var template = new RestTemplate();
        return template.exchange(url,
                GET,
                HttpEntity.EMPTY,
                CEPDataResponse.class).getBody();
    }

    public CoordinatesDTO getCoordinates(String address){
        String url = getCoordinatesRequestUrl(address);

        logger.info("Fetching coordinate data at: " + url);

        var template = new RestTemplate();
        var a = template.exchange(url,
                GET,
                HttpEntity.EMPTY,
                GeocodingResponse.class);

        if(a.getStatusCode().equals(GeocodingResponse.NO_RESULTS_STATUS)){
            logger.error("No results returned for coordinate data");
            throw new RuntimeException(""); // mudar dps
        }

        return a.getBody().getResults().get(0).getGeometry().getLocation();
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
