package br.pucpr.petapi.lib.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/location.properties")
@ConfigurationProperties("location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationSettings {
    private String googleGoecodingUrl;
    private String cepApiUrl;
}
