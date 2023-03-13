package br.pucpr.petapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* TODO
- implementar classe utilitária para trabalhar com apis externas e dados de localização
- implementar o CEPValidator
- criação de adoptionprofile
 */

@SpringBootApplication
public class PetApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetApiApplication.class, args);
    }

}
