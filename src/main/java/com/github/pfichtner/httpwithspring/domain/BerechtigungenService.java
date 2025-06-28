package com.github.pfichtner.httpwithspring.domain;

import java.util.Optional;

public interface BerechtigungenService {

	Optional<Berechtigung> load(BerechtigungsId id);

	boolean save(Berechtigung berechtigung);

	boolean delete(BerechtigungsId id);

}