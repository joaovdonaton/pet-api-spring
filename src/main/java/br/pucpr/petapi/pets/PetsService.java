package br.pucpr.petapi.pets;

import br.pucpr.petapi.lib.error.ResourceDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PetsService {
    private final PetsRepository repository;

    public PetsService(PetsRepository repository) {
        this.repository = repository;
    }

    public boolean existsByName(String name){
        return repository.existsByName(name);
    }
}
