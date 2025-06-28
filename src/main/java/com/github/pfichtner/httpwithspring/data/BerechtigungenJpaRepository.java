package com.github.pfichtner.httpwithspring.data;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;

public interface BerechtigungenJpaRepository extends JpaRepository<Berechtigung, UUID> {
}
