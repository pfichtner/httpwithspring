package com.github.pfichtner.httpwithspring;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.UUID;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class BerechtigungenProcessIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void testPutIsIdempotent() throws Exception {
		UUID uuid = UUID.randomUUID();

		for (int i = 0; i < 3; i++) {
			String putPayload = """
					{
						"foo": "foo-idempotent",
						"bar": 123,
						"foobar": "foobar-idempotent"
					}
					""";
			ResultActions result = mockMvc.perform(put("/berechtigungen/" + uuid) //
					.contentType(APPLICATION_JSON) //
					.content(putPayload));
			if (i == 0) {
				result.andExpect(status().isCreated()) //
						.andExpect(header().string("Location", hasPath("/berechtigungen/" + uuid)));
			} else {
				result.andExpect(content().string("")) //
						.andExpect(status().isNoContent());
			}
		}

		// Confirm GET returns correct data
		mockMvc.perform(get("/berechtigungen/" + uuid)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.foo").value("foo-idempotent")) //
				.andExpect(jsonPath("$.bar").value(123)) //
				.andExpect(jsonPath("$.foobar").value("foobar-idempotent"));
	}

	@Test
	void testCreateAndRetrieveBerechtigung() throws Exception {
		UUID uuid = UUID.randomUUID();
		mockMvc.perform(get("/berechtigungen/" + uuid)) //
				.andExpect(status().isNotFound());
		String putPayload;
		putPayload = """
				{
					"foo": "foo-value",
					"bar": 42,
					"foobar": "foobar-value"
				}
				""";
		mockMvc.perform(put("/berechtigungen/" + uuid) //
				.contentType(APPLICATION_JSON) //
				.content(putPayload)) //
				.andExpect(status().isCreated());

		mockMvc.perform(get("/berechtigungen/" + uuid)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.foo").value("foo-value")) //
				.andExpect(jsonPath("$.bar").value(42)) //
				.andExpect(jsonPath("$.foobar").value("foobar-value"));

		// Update with PUT using new data
		putPayload = """
				{
					"foo": "new-foo-value",
					"bar": 99,
					"foobar": "new-foobar-value"
				}
				""";

		mockMvc.perform(put("/berechtigungen/" + uuid) //
				.contentType(APPLICATION_JSON) //
				.content(putPayload)) //
				.andExpect(content().string("")) //
				.andExpect(status().isNoContent());

		// Check GET returns updated content
		mockMvc.perform(get("/berechtigungen/" + uuid)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.foo").value("new-foo-value")) //
				.andExpect(jsonPath("$.bar").value(99)) //
				.andExpect(jsonPath("$.foobar").value("new-foobar-value"));
	}

	@Test
	void testGetWithInvalidUuidReturns400() throws Exception {
		mockMvc.perform(get("/berechtigungen/not-a-uuid")) //
				.andExpect(status().isBadRequest());
	}

	@Test
	void testDeleteExistingBerechtigung() throws Exception {
		UUID uuid = UUID.randomUUID();

		String putPayload = """
				{
					"foo": "foo-to-delete",
					"bar": 123,
					"foobar": "foobar-to-delete"
				}
				""";

		// Create entry
		mockMvc.perform(put("/berechtigungen/" + uuid) //
				.contentType(APPLICATION_JSON) //
				.content(putPayload)) //
				.andExpect(status().isCreated());

		// Confirm GET works before delete
		mockMvc.perform(get("/berechtigungen/" + uuid)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.foo").value("foo-to-delete"));

		// Delete entry, HttpStatus will be 204
		mockMvc.perform(delete("/berechtigungen/" + uuid)) //
				.andExpect(content().string("")) //
				.andExpect(status().isNoContent());

		// Attempt to delete already deleted entry, HttpStatus will be 404
		mockMvc.perform(delete("/berechtigungen/" + uuid)) //
				.andExpect(status().isNotFound());

		// Confirm GET returns 404
		mockMvc.perform(get("/berechtigungen/" + uuid)) //
				.andExpect(status().isNotFound());
	}

	private static TypeSafeMatcher<String> hasPath(String expectedPath) {
		return new TypeSafeMatcher<>() {
			@Override
			protected boolean matchesSafely(String actual) {
				try {
					return expectedPath.equals(URI.create(actual).getPath());
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("a URL with path ").appendValue(expectedPath);
			}

			@Override
			protected void describeMismatchSafely(String actual, Description mismatchDescription) {
				try {
					mismatchDescription.appendText("path was ").appendValue(URI.create(actual).getPath());
				} catch (Exception e) {
					mismatchDescription.appendText("was not a valid URI: ").appendValue(actual);
				}
			}
		};
	}

}
