package com.github.pfichtner.httpwithspring.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.pfichtner.httpwithspring.data.BerechtigungenJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class DefaultBerechtigungenService implements BerechtigungenService {

	private final BerechtigungenJpaRepository repo;

	public Optional<Berechtigung> load(UUID id) {
		return repo.findById(id);
	}

	public boolean save(Berechtigung berechtigung) {
		boolean found = load(berechtigung.getId()).isPresent();
		repo.save(berechtigung);
		return !found;
	}

	@Override
	public boolean delete(UUID id) {
		boolean found = load(id).isPresent();
		repo.deleteById(id);
		return found;
	}

}
