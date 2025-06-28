package com.github.pfichtner.httpwithspring.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.domain.BerechtigungsId;

public interface BerechtigungenJpaRepository extends JpaRepository<Berechtigung, BerechtigungsId> {
}
