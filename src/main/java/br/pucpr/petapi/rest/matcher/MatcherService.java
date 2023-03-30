package br.pucpr.petapi.rest.matcher;

import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfileService;
import br.pucpr.petapi.rest.matcher.dto.MatchingResultDTO;
import br.pucpr.petapi.rest.petTypes.PetTypeService;
import br.pucpr.petapi.rest.pets.PetsService;
import br.pucpr.petapi.rest.users.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatcherService {
    private final UsersService usersService;
    private final PetsService petsService;
    private final PetTypeService petTypeService;
    private final AdoptionProfileService adoptionProfileService;

    public MatcherService(UsersService usersService, PetsService petsService, PetTypeService petTypeService, AdoptionProfileService adoptionProfileService) {
        this.usersService = usersService;
        this.petsService = petsService;
        this.petTypeService = petTypeService;
        this.adoptionProfileService = adoptionProfileService;
    }

    /**
     * Retorna matches para o usuário atualmente autenticado. Pega os pets ordenados por distância e filtrados por viewed
     * e em seguida faz a ordenação baseado nas preferências de tipo de pet do usuário.
     */
    @Transactional
    public List<MatchingResultDTO> getNextMatches(int limit){
        var currentAuth = usersService.getCurrentAuth();

        var pets = new ArrayList<>(petsService.findPetsSortByDistance(currentAuth.getAdoptionProfile(), limit, true));

        pets.sort((a, b) -> {
            //distâncias menores que 1000 metros são consideradas iguais, então ordenar por preferência
            if(a.getDistance() - b.getDistance() > 1000) return 0;

            var preferredTypes = currentAuth.getAdoptionProfile().getPreferredPetTypes();
            var hasA = preferredTypes.contains(a.getType());
            var hasB = preferredTypes.contains(b.getType());

            // caso não haja preferência, ou seja por todos, não ordernar por preferência
            if(preferredTypes.isEmpty() || preferredTypes.containsAll(petTypeService.getAllPetTypeNames())) return 0;

            if(hasB && hasA) return 0;
            if(hasA) return -1;
            return 1;
        });

        // atualizar lista de viewed
        pets.forEach(petWithDistance -> currentAuth.getAdoptionProfile().addViewedId(petWithDistance.getId()));

        return pets.stream().map(petWithDistance -> new MatchingResultDTO(
                petWithDistance.getName(),
                petWithDistance.getNickname(),
                petWithDistance.getAge(),
                petWithDistance.getDescription(),
                petWithDistance.getType(),
                petWithDistance.getUser().getUsername(),
                petWithDistance.getUser().getAdoptionProfile().getCity(),
                petWithDistance.getUser().getAdoptionProfile().getState(),
                petWithDistance.getDistance()
        )).toList();
    }
}
