package com.github.pfichtner.httpwithspring.data;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;

@RepositoryRestResource(path = "berechtigungen", collectionResourceRel = "berechtigungen")
interface BerechtigungenJpaRepository extends JpaRepository<Berechtigung, UUID> {
}
