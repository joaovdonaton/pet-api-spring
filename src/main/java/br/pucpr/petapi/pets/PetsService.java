package br.pucpr.petapi.pets;

import br.pucpr.petapi.petTypes.PetTypeService;
import br.pucpr.petapi.pets.dto.PetRegisterDTO;
import br.pucpr.petapi.users.User;
import br.pucpr.petapi.users.UsersService;
import br.pucpr.petapi.users.dto.UserInfoDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PetsService {
    private final PetsRepository repository;
    private final UsersService usersService;
    private final PetTypeService petTypeService;

    public PetsService(PetsRepository repository, UsersService usersService, PetTypeService petTypeService) {
        this.repository = repository;
        this.usersService = usersService;
        this.petTypeService = petTypeService;
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
        Pageable pageable = PageRequest.of(page,
                limit,
                setSortAscDesc(ascDesc, Sort.by(sortBy)));

        return repository.findAll(pageable).toList();
    }

    private Sort setSortAscDesc(String ascDesc, Sort s){
        return ascDesc.equals("asc") ? s.ascending() : s.descending();
    }
}
