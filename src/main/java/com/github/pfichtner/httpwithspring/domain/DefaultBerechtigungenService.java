package com.github.pfichtner.httpwithspring.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.pfichtner.httpwithspring.data.BerechtigungenJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class DefaultBerechtigungenService implements BerechtigungenService {

	private final BerechtigungenJpaRepository repo;

	public Optional<Berechtigung> load(BerechtigungsId id) {
		return repo.findById(id);
	}

	public boolean save(Berechtigung berechtigung) {
		Optional<Berechtigung> optional = load(berechtigung.getId());
		repo.save(berechtigung);
		return !optional.isPresent();
	}

	@Override
	public boolean delete(BerechtigungsId id) {
		Optional<Berechtigung> optional = load(id);
		repo.deleteById(id);
		return optional.isPresent();
	}

}
