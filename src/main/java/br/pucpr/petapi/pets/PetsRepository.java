package br.pucpr.petapi.pets;

import org.springdoc.core.converters.models.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetsRepository extends JpaRepository<Pet, UUID> {
    boolean existsByName(String name);
}
