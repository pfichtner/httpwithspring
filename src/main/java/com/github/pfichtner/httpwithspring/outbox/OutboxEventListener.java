package com.github.pfichtner.httpwithspring.outbox;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxEventListener {

	private final OutboxEventRepository eventRepository;

	@TransactionalEventListener(phase = BEFORE_COMMIT)
	void onEvent(OutboxEvent outboxEvent) {
		eventRepository.save(outboxEvent);
	}

}
