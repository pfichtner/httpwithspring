package com.github.pfichtner.httpwithspring.domain;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Repository;

import com.github.pfichtner.httpwithspring.data.BerechtigungEntity;
import com.github.pfichtner.httpwithspring.data.BerechtigungenJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class DefaultBerechtigungenRepository implements Berechtigungen {

	private final BerechtigungenJpaRepository jpaDelegate;

	public Optional<Berechtigung> load(BerechtigungsId id) {
		return jpaDelegate.findById(id.value()).map(BerechtigungEntity::toDomain);
	}

	public boolean save(Berechtigung berechtigung) {
		boolean wasPresent = load(berechtigung.getId()).isPresent();
		jpaDelegate.save(BerechtigungEntity.fromDomain(berechtigung));
		return !wasPresent;
	}

	@Override
	public boolean delete(BerechtigungsId id) {
		AtomicBoolean deleted = new AtomicBoolean();
		load(id).ifPresent(b -> {
			jpaDelegate.deleteById(b.getId().value());
			deleted.set(true);
		});
		return deleted.get();
	}

}
