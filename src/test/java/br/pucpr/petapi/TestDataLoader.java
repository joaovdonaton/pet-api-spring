package br.pucpr.petapi;

import br.pucpr.petapi.lib.location.dto.response.CEPDataResponse;
import br.pucpr.petapi.lib.location.dto.response.geocoding.CoordinatesDTO;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequest;
import br.pucpr.petapi.rest.adoptionRequests.enums.Status;
import br.pucpr.petapi.rest.petTypes.PetType;
import br.pucpr.petapi.rest.pets.Pet;
import br.pucpr.petapi.rest.pets.PetsService;
import br.pucpr.petapi.rest.pets.dto.PetWithDistance;
import br.pucpr.petapi.rest.users.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;


/**
 * Dados dessa classe estão organizados pelo índice.
 * e.g os dados do índice 0 do cepDataResponses complementa o do índice 0 do coordinatesDTOs
 * AVISO: os dados já existentes NÃO DEVEM ser alterados, pois vão quebrar certos testes. Apenas adicionar novos.
 */
@Data
public class TestDataLoader {
    private final List<CEPDataResponse> cepDataResponses = List.of(
            new CEPDataResponse(
                    "80250-220",
                    "Avenida Visconde de Guarapuava",
                    "de 3337/3338 a 4379/4380",
                    "Centro",
                    "Curitiba",
                    "PR",
                    "4106902",
                    "",
                    "41",
                    "7535"
            ),
            new CEPDataResponse(
                    "82410-530",
                    "Avenida Doutor Eugênio Bertolli",
                    "",
                    "Santa Felicidade",
                    "Curitiba",
                    "PR",
                    "4106902",
                    "",
                    "41",
                    "7535"
            )
    );

    private final List<CoordinatesDTO> coordinatesDTOs = List.of(
            new CoordinatesDTO(
                    "-25.437242700000",
                    "-49.269966500000"
            ),
            new CoordinatesDTO(
                    "-25.400720900000",
                    "-49.334472800000"
            )
    );

    private final List<AdoptionProfile> adoptionProfiles = List.of(
            new AdoptionProfile(
                    null,
                    "80250-220",
                    "I like cats and dogs and cats and dogs and cats and dogs and cats and dogs",
                    false,
                    "PR",
                    "Curitiba",
                    "Centro",
                    new BigDecimal(coordinatesDTOs.get(0).getLat()),
                    new BigDecimal(coordinatesDTOs.get(0).getLng()),
                    null,
                    null
            )
    );

    private final List<Pet> pets = List.of(
            new Pet("Jimmy",
                    "Jim",
                    15,
                    "",
                    new User("", "", ""),
                    null,
                    new PetType("cat")),
            new Pet("Timmy",
                    "Tim",
                    9,
                    "",
                    new User("", "", ""),
                    null,
                    new PetType("dog")),
            new Pet("Limmy",
                    "Lim",
                    1,
                    "",
                    new User("", "", ""),
                    null,
                    new PetType("cat"))
    );

    private final List<PetWithDistance> petsWithDistance = List.of(
        PetWithDistance.fromPet(pets.get(0), 5000),
        PetWithDistance.fromPet(pets.get(1), 5500),
        PetWithDistance.fromPet(pets.get(2), 24000)
    );

    public Set<AdoptionRequest> generateAdoptionRequests(Pet pet, User sender, User receiver, int count){
        var result = new HashSet<AdoptionRequest>();
        for(int i = 0; i < count; i++){
            result.add(new AdoptionRequest( UUID.randomUUID(),
                    pet, sender, receiver, "", "", getRandomRequestStatus()
            ));
        }
        return result;
    }

    private Status getRandomRequestStatus(){
        return switch(new Random().nextInt(0, 3)){
            case 0 -> Status.PENDING;
            case 1 -> Status.ACCEPTED;
            case 2 -> Status.REJECTED;
            default -> throw new IllegalStateException("Unexpected value from random number generator");
        };
    }
}
