package com.github.pfichtner.httpwithspring.outbox.publisher;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

@ExtendWith(MockitoExtension.class)
class OutboxPublisherTest {

	@Mock
	OutboxEventRepository outboxRepo;

	@InjectMocks
	OutboxPublisher publisher;

	@Test
	void deletesPublishedEventsOlderThan7Days() {
		publisher.cleanUpOldEvents();
		Instant expectedCutoff = Instant.now().minus(7, DAYS);
		// Allow a few ms jitter due to Instant.now()
		verify(outboxRepo).deleteByPublishedIsTrueAndCreatedAtBefore(
				argThat(c -> Duration.between(c, expectedCutoff).abs().toMillis() < 1000));
	}
}
