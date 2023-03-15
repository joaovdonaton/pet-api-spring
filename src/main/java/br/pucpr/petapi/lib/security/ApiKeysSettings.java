package br.pucpr.petapi.lib.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/apikeys.properties")
@ConfigurationProperties("keys")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeysSettings {
    private String googleApi;
}
