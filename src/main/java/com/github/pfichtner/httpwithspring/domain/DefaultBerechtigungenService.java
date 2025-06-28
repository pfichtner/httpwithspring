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

	public void save(Berechtigung berechtigung) {
		repo.save(berechtigung);
	}

	@Override
	public boolean delete(UUID id) {
		Optional<Berechtigung> optional = load(id);
		repo.deleteById(id);
		return optional.isPresent();
	}

}
