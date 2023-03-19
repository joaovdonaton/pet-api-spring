package br.pucpr.petapi.petTypes;

import br.pucpr.petapi.lib.error.ResourceDoesNotExistException;
import org.springframework.http.HttpStatus;
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

    public PetType findPetTypeByName(String name){
        return repository.findByName(name).orElseThrow(() ->
                new ResourceDoesNotExistException("Could not find pet type named ["+name+"]", HttpStatus.NOT_FOUND));
    }
}
