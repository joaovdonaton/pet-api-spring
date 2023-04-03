package br.pucpr.petapi.lib.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/messages.properties")
@ConfigurationProperties("message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSettings {
    private String resourceDoesNotExist;
    private String validationFailure;
}
