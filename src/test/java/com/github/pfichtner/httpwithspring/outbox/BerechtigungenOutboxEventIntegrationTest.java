package com.github.pfichtner.httpwithspring.outbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;
import com.github.pfichtner.httpwithspring.outbox.publisher.OutboxPublisher;

@SpringBootTest
@AutoConfigureMockMvc
// prevent forwarding of unpublished events by "disabling" OutboxPublisher
@ImportAutoConfiguration(exclude = OutboxPublisher.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
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

		var expected = new OutboxEvent() //
				.setAggregateType("Berechtigung") //
				.setType("created") //
				.setAggregateId(uuid.toString()) //
		;
		assertThat(outboxRepository.findUnpublished()).singleElement() //
				.satisfies(e -> assertThat(e.getId()).isNotNull()) //
				.usingRecursiveComparison().ignoringFields("id", "createdAt").isEqualTo(expected);
	}

}
