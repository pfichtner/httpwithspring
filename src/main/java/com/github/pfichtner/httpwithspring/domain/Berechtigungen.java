package com.github.pfichtner.httpwithspring.domain;

import java.util.Optional;

public interface Berechtigungen {

	Optional<Berechtigung> load(BerechtigungsId id);

	void save(Berechtigung berechtigung);

	boolean delete(BerechtigungsId berechtigung);

}