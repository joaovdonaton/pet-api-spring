package br.pucpr.petapi.matcher;

import br.pucpr.petapi.adoptionProfiles.AdoptionProfileService;
import br.pucpr.petapi.matcher.dto.MatchingResultDTO;
import br.pucpr.petapi.petTypes.PetTypeService;
import br.pucpr.petapi.pets.PetsService;
import br.pucpr.petapi.users.UsersService;
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
     * @param limit
     * @return
     *
     */
    @Transactional
    public List<MatchingResultDTO> getNextMatches(int limit){
        var currentAuth = usersService.getCurrentAuth();

        var pets = new ArrayList<>(petsService.findPetsSortByDistance(currentAuth.getAdoptionProfile(), limit, true));

        pets.sort((a, b) -> {
            //distâncias menores que 1000 metros são consideradas iguais, então ordenar por preferência
            if(a.getDistance() - b.getDistance() > 1000) return 0;

            var preferredTypes = currentAuth.getAdoptionProfile().getPreferredPetTypes();
            var hasA = preferredTypes.contains(a.getPetType());
            var hasB = preferredTypes.contains(b.getPetType());

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
                petWithDistance.getPetType(),
                petWithDistance.getUser().getUsername(),
                petWithDistance.getUser().getAdoptionProfile().getCity(),
                petWithDistance.getUser().getAdoptionProfile().getState(),
                petWithDistance.getDistance()
        )).toList();
    }
}
