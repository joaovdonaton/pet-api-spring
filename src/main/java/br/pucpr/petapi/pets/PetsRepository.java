package br.pucpr.petapi.pets;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetsRepository extends JpaRepository<Pet, UUID> {
}
