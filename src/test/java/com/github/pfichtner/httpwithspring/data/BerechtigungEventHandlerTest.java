package com.github.pfichtner.httpwithspring.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.bean.override.mockito.MockReset.AFTER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

import jakarta.servlet.ServletException;

@SpringBootTest
@AutoConfigureMockMvc
class BerechtigungEventHandlerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	BerechtigungenJpaRepository berechtigungenRepository;

	@MockitoSpyBean(reset = AFTER)
	OutboxEventRepository eventRepository;

	@Test
	void doesRollbackOnException() throws Exception {
		String message = "some text";
		doThrow(new RuntimeException(message)).when(eventRepository).save(any(OutboxEvent.class));

		UUID uuid = UUID.randomUUID();
		String putPayload = """
				{
					"id": "%s",
					"foo": "should not get created since we can't persist the event",
					"bar": 123,
					"foobar": "foobar"
				}
				""".formatted(uuid);

		var exception = assertThrows(ServletException.class, () -> {
			mockMvc.perform(put("/berechtigungen/" + uuid) //
					.contentType(APPLICATION_JSON) //
					.content(putPayload));
		});
		assertThat(exception).hasRootCauseMessage(message);
		assertThat(berechtigungenRepository.findAll()).isEmpty();
	}

}
