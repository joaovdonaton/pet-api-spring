package br.pucpr.petapi.rest.petTypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PetTypeRepository extends JpaRepository<PetType, UUID> {
    boolean existsByName(String name);
    Optional<PetType> findByName(String name);
}
