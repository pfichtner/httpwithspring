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

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEventRepository;

import jakarta.servlet.ServletException;

@SpringBootTest
@AutoConfigureMockMvc
class BerechtigungEventHandler2Test {

	@Autowired
	MockMvc mockMvc;

	@MockitoSpyBean(reset = AFTER)
	BerechtigungenJpaRepository berechtigungenRepository;

	@Autowired
	OutboxEventRepository eventRepository;

	@Test
	void doesRollbackOnException() throws Exception {
		String message = "some text";
		doThrow(new RuntimeException(message)).when(berechtigungenRepository).save(any(Berechtigung.class));

		UUID uuid = UUID.randomUUID();
		String putPayload = """
				{
				  "id": "%s",
				  "foo": "should not persist",
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
		assertThat(eventRepository.findAll()).isEmpty();
	}

}
