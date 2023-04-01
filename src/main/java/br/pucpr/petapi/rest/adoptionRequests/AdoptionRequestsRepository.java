package br.pucpr.petapi.rest.adoptionRequests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdoptionRequestsRepository extends JpaRepository<AdoptionRequest, UUID> {
}
