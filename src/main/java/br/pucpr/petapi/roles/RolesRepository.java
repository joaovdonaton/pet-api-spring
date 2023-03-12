package br.pucpr.petapi.roles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolesRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(String name);
    Role findByName(String name);
}
