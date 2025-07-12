package com.github.pfichtner.httpwithspring.outbox.publisher;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.github.pfichtner.httpwithspring.outbox.MessagePublisher;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

@SpringBootTest
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class OutboxPublisherScheduledTest {

	@Autowired
	OutboxEventRepository outboxRepo;

	@MockitoBean
	MessagePublisher messagePublisher;

	@Autowired
	OutboxPublisher outboxPublisher;

	@Test
	void scheduledPublishEventsRunsAndPublishes() throws InterruptedException {
		var savedEvent = outboxRepo.save(new OutboxEvent().setPublished(false).setAggregateType("test")
				.setType("created").setAggregateId("123"));
		assertThat(outboxRepo.findById(savedEvent.getId()))
				.hasValueSatisfying(v -> assertThat(v.isPublished()).isFalse());
		await().untilAsserted(() -> {
			verify(messagePublisher).publish(argThat( //
					// allow slight difference in createdAt (e.g. within 1 millisecond)
					event -> event.getId().equals(savedEvent.getId()) //
							&& event.isPublished() //
							&& event.getAggregateType().equals(savedEvent.getAggregateType())
							&& event.getAggregateId().equals(savedEvent.getAggregateId())
							&& event.getType().equals(savedEvent.getType()) && abs(event.getCreatedAt().toEpochMilli()
									- savedEvent.getCreatedAt().toEpochMilli()) < 1));
		});
		assertThat(outboxRepo.findById(savedEvent.getId()))
				.hasValueSatisfying(v -> assertThat(v.isPublished()).isTrue());
	}

}
