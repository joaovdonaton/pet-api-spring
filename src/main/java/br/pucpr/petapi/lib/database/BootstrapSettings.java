package br.pucpr.petapi.lib.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("/bootstrapdata.properties")
@ConfigurationProperties("data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BootstrapSettings {
    private List<String> roles;
    private List<String> petTypes;
    private List<String> usernames;
    private String defaultPassword;
    private List<String> names;
    private String petDefaultDescription;

    // esses campos devem todos ter o mesmo tamanho
    private List<String> petNames;
    private List<String> petNicknames;
    private List<Integer> petAges;
    private List<String> ownerUsernames;
    private List<String> petAnimalTypes;
}
