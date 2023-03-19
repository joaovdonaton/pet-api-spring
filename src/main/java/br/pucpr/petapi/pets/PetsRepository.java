package br.pucpr.petapi.pets;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PetsRepository extends JpaRepository<Pet, UUID> {
    boolean existsByName(String name);
}
