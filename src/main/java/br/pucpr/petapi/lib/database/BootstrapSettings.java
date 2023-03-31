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
    private String defaultPassword;
    private String petDefaultDescription;
    private String profileDefaultDescription;
    private List<String> profileDefaultPreferredTypes;

    // devem ter o mesmo tamanho (criação dos users de teste)
    private List<String> usernames;
    private List<String> names;

    // também devem ter o mesmo tamanho (criação dods pets de teste)
    private List<String> petNames;
    private List<String> petNicknames;
    private List<Integer> petAges;
    private List<String> ownerUsernames;
    private List<String> petAnimalTypes;

    // mesmo tamanho (perfis)
    private List<String> profileUsers;
    private List<String> profileCeps;
    private List<String> profileNewPetOwner;

}
