package br.pucpr.petapi.petTypes;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetTypeService {
    private final PetTypeRepository repository;

    public PetTypeService(PetTypeRepository repository) {
        this.repository = repository;
    }

    public Set<String> getAllPetTypeNames(){
        return repository.findAll().stream().map(PetType::getName).collect(Collectors.toSet());
    }
}
