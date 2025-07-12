package com.github.pfichtner.httpwithspring.outbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;
import com.github.pfichtner.httpwithspring.outbox.publisher.OutboxPublisher;

@SpringBootTest
@AutoConfigureMockMvc
// prevent forwarding of unpublished events by "disabling" OutboxPublisher
@ImportAutoConfiguration(exclude = OutboxPublisher.class)
class BerechtigungenOutboxEventIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	OutboxEventRepository outboxRepository;

	@Test
	void onInsertMatchingEvent() throws Exception {
		UUID uuid = UUID.randomUUID();
		String putPayload = """
				{
					"id": "%s",
					"foo": "foo-idempotent",
					"bar": 123,
					"foobar": "foobar-idempotent"
				}
				""".formatted(uuid);
		mockMvc.perform(put("/berechtigungen/" + uuid) //
				.contentType(APPLICATION_JSON) //
				.content(putPayload));

		OutboxEvent expectedEvent = OutboxEvent.builder() //
				.aggregateType("Berechtigung") //
				.type("BerechtigungCreated") //
				.aggregateId(uuid.toString()) //
				.build();
		assertThat(outboxRepository.findUnpublished()).singleElement() //
				.satisfies(e -> assertThat(e.id()).isNotNull()) //
				.usingRecursiveComparison().ignoringFields("id", "timestamp").isEqualTo(expectedEvent);
	}

}
