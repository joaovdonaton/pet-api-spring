package br.pucpr.petapi.lib.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/security.properties")
@ConfigurationProperties("security")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecuritySettings {
    private String secret;
    private String issuer;
}
