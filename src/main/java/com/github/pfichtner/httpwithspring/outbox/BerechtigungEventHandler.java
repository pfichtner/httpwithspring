package com.github.pfichtner.httpwithspring.outbox;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;

import lombok.RequiredArgsConstructor;

@Component
@RepositoryEventHandler(Berechtigung.class)
@RequiredArgsConstructor
class BerechtigungEventHandler {

	@HandleBeforeCreate
	public void handleCreate(Berechtigung entity) {
		entity.registerEvent(new OutboxEvent() //
				.setAggregateType("Berechtigung") //
				.setType("created") //
				.setAggregateId(entity.getId().toString()) //
		);
	}

}
