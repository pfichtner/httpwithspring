package com.github.pfichtner.httpwithspring.outbox.publisher;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.pfichtner.httpwithspring.outbox.MessagePublisher;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

	private final OutboxEventRepository outboxRepo;

	private final MessagePublisher messagePublisher;

	@Scheduled(fixedDelay = 5000)
	public void publishEvents() {
		for (OutboxEvent event : outboxRepo.findUnpublished()) {
			messagePublisher.publish(event);
			outboxRepo.save(event.setPublished(true));
		}
	}

	@Scheduled(cron = "0 0 * * * *")
	public void cleanUpOldEvents() {
		outboxRepo.deleteByPublishedIsTrueAndCreatedAtBefore(Instant.now().minus(7, DAYS));
	}

}
