package com.github.pfichtner.httpwithspring.outbox;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

import lombok.RequiredArgsConstructor;

@Component
@RepositoryEventHandler(Berechtigung.class)
@RequiredArgsConstructor
public class BerechtigungEventHandler {

	private final OutboxEventRepository outboxRepo;

	@HandleAfterCreate
	public void handleCreate(Berechtigung entity) {
		outboxRepo.save(OutboxEvent.builder() //
				.aggregateType("Berechtigung") //
				.type("BerechtigungCreated") //
				.aggregateId(entity.getId().toString()) //
				.build());
	}

}
