package com.github.pfichtner.httpwithspring.data;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BerechtigungenJpaRepository extends JpaRepository<BerechtigungEntity, UUID> {
}
