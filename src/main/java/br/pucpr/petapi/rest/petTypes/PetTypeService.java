package br.pucpr.petapi.rest.petTypes;

import br.pucpr.petapi.lib.error.MessageSettings;
import br.pucpr.petapi.lib.error.exceptions.ResourceDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetTypeService {
    private final PetTypeRepository repository;
    private final MessageSettings messageSettings;

    public PetTypeService(PetTypeRepository repository, MessageSettings messageSettings) {
        this.repository = repository;
        this.messageSettings = messageSettings;
    }

    public Set<String> getAllPetTypeNames(){
        return repository.findAll().stream().map(PetType::getName).collect(Collectors.toSet());
    }

    public PetType findPetTypeByName(String name){
        return repository.findByName(name).orElseThrow(() ->
                new ResourceDoesNotExistException(messageSettings.getResourceDoesNotExist(),
                        "Could not find pet type named ["+name+"]", HttpStatus.NOT_FOUND));
    }
}
