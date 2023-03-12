package br.pucpr.petapi.petTypes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetTypeRepository extends JpaRepository<PetType, UUID> {
}
