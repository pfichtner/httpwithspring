package com.github.pfichtner.httpwithspring.domain;

import java.util.Optional;
import java.util.UUID;

public interface BerechtigungenService {

	Optional<Berechtigung> load(UUID id);

	void save(Berechtigung berechtigung);

	boolean delete(UUID id);

}