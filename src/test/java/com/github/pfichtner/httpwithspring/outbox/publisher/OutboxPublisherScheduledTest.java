package com.github.pfichtner.httpwithspring.outbox.publisher;

import static java.lang.Math.abs;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.github.pfichtner.httpwithspring.outbox.MessagePublisher;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

@SpringBootTest
class OutboxPublisherScheduledTest {

	@Autowired
	OutboxEventRepository outboxRepo;

	@MockitoBean
	MessagePublisher messagePublisher;

	@Autowired
	OutboxPublisher outboxPublisher;

	@Test
	void scheduledPublishEventsRunsAndPublishes() throws InterruptedException {
		var saved = outboxRepo.save(new OutboxEvent().setPublished(false).setAggregateType("test").setType("created")
				.setAggregateId("123"));
		assertThat(outboxRepo.findById(saved.getId())).hasValueSatisfying(e -> assertThat(e.isPublished()).isFalse());
		await().untilAsserted(() -> {
			verify(messagePublisher).publish(argThat( //
					e -> e.getId().equals(saved.getId()) //
							&& e.getAggregateType().equals(saved.getAggregateType())
							&& e.getAggregateId().equals(saved.getAggregateId()) && e.getType().equals(saved.getType())
			// allow slight difference in createdAt (e.g. within 1 millisecond)
							&& abs(e.getCreatedAt().toEpochMilli() - saved.getCreatedAt().toEpochMilli()) < 1));
		});

		assertThat(outboxRepo.findAll()).singleElement().satisfies(e -> {
			assertThat(e.getId()).isEqualTo(saved.getId());
			assertThat(e.getAggregateType()).isEqualTo(saved.getAggregateType());
			assertThat(e.getAggregateId()).isEqualTo(saved.getAggregateId());
			// allow slight difference in createdAt (e.g. within 1 millisecond)
			assertThat(e.getCreatedAt()).isCloseTo(saved.getCreatedAt(), within(1, MILLIS));
			assertThat(e.isPublished()).isTrue();
		});
	}

}
