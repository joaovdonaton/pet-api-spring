package br.pucpr.petapi.roles;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RolesService {
    private RolesRepository repository;

    public RolesService(RolesRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Role findByName(String name){
        return repository.findByName(name);
    }
}
