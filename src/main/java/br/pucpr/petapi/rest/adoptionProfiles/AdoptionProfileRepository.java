package br.pucpr.petapi.rest.adoptionProfiles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdoptionProfileRepository extends JpaRepository<AdoptionProfile, UUID> {
}
