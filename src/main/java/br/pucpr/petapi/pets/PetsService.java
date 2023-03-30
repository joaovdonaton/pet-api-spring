package br.pucpr.petapi.pets;

import br.pucpr.petapi.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.adoptionProfiles.AdoptionProfileService;
import br.pucpr.petapi.adoptionProfiles.dto.AdoptionProfileWithDistanceDTO;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.petTypes.PetTypeService;
import br.pucpr.petapi.pets.dto.PetRegisterDTO;
import br.pucpr.petapi.pets.dto.PetWithDistance;
import br.pucpr.petapi.users.User;
import br.pucpr.petapi.users.UsersService;
import br.pucpr.petapi.users.dto.UserInfoDTO;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PetsService {
    private final PetsRepository repository;
    private final UsersService usersService;
    private final PetTypeService petTypeService;
    private final AdoptionProfileService adoptionProfileService;
    private final LocationUtils locationUtils;

    public PetsService(PetsRepository repository, UsersService usersService, PetTypeService petTypeService, AdoptionProfileService adoptionProfileService, LocationUtils locationUtils) {
        this.repository = repository;
        this.usersService = usersService;
        this.petTypeService = petTypeService;
        this.adoptionProfileService = adoptionProfileService;
        this.locationUtils = locationUtils;
    }

    public boolean existsByName(String name){
        return repository.existsByName(name);
    }

    public Pet createPet(PetRegisterDTO petRegisterDTO) {
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = usersService.findByUsername(currentUserInfo.getUsername());

        Pet p = new Pet(
                petRegisterDTO.getName(),
                petRegisterDTO.getNickname(),
                petRegisterDTO.getAge(),
                petRegisterDTO.getDescription(),
                u,
                LocalDateTime.now(),
                petTypeService.findPetTypeByName(petRegisterDTO.getType())
        );

        return repository.save(p);
    }

    public List<Pet> searchPet(int limit, int page, String sortBy, String ascDesc){
        if(sortBy.equals("distance")){
            return null;
        }
        else {
            Pageable pageable = PageRequest.of(page,
                    limit,
                    setSortAscDesc(ascDesc, Sort.by(sortBy)));
            return repository.findAll(pageable).toList();
        }
    }

    /**
     * @param reference perfil para qual as distâncias serão calculadas
     * @param limit limite de pets para retornar
     * @return lista com pets ordenados por distância
     */
    public List<PetWithDistance> findPetsSortByDistance(AdoptionProfile reference, int limit){
        List<PetWithDistance> result = new ArrayList<>();

        for (int i = 0; i < AdoptionProfile.getLevels()+1; i++){
            for (var p : findAllByLevel(i, reference)){
                // não adicionar ja adicionados
                if(result.stream().noneMatch(pet -> pet.getPet().equals(p)))
                    result.add(new PetWithDistance(p,
                            locationUtils.getDirectDistanceBetweenProfiles(p.getUser().getAdoptionProfile(), reference)));

                if(result.size() == limit) break;
            }

            if(result.size() == limit) break;
        }

        return result.stream().sorted(Comparator.comparingInt(PetWithDistance::getDistance)).toList();
    }

    /**
     * segue as mesmas regras do método AdoptionProfileService.findAllByLevel
     */
    public List<Pet> findAllByLevel(int level, AdoptionProfile reference){
        List<Pet> result = new ArrayList<>();
        var profiles = adoptionProfileService.findAllByLevel(level, reference);

        for (var p: profiles){
            result.addAll(p.getUser().getPets());
        }

        return result;
    }

    // encontra todos os pets do usuário logado
    public List<Pet> findAllPetsBelongingToUser(){
        UserInfoDTO currentUserInfo = (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var pets = usersService.findByUsername(currentUserInfo.getUsername()).getPets().stream().toList();

        return pets;
    }

    private Sort setSortAscDesc(String ascDesc, Sort s){
        return ascDesc.equals("asc") ? s.ascending() : s.descending();
    }
}
