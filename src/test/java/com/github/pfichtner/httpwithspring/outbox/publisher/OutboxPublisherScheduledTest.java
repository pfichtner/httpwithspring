package com.github.pfichtner.httpwithspring.outbox.publisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
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
		var event = outboxRepo
				.save(new OutboxEvent().setAggregateType("test").setType("created").setAggregateId("123"));
		assertThat(outboxRepo.findById(event.getId())).hasValueSatisfying(e -> assertThat(e.isPublished()).isFalse());
		event.setPublished(true);
		await().untilAsserted(() -> verify(messagePublisher).publish(eq(event)));
		assertThat(outboxRepo.findAll()).singleElement().isEqualTo(event);
	}

}
