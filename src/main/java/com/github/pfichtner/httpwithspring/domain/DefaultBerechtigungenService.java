package com.github.pfichtner.httpwithspring.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class DefaultBerechtigungenService implements BerechtigungenService {

	private final Berechtigungen berechtigungen;

	public Optional<Berechtigung> load(BerechtigungsId id) {
		return berechtigungen.load(id);
	}

	public boolean save(Berechtigung berechtigung) {
		return berechtigungen.save(berechtigung);
	}

	@Override
	public boolean delete(BerechtigungsId id) {
		return berechtigungen.delete(id);
	}

}
