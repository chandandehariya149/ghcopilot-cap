package com.todoapp;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TodoService todos;

	@BeforeEach
	void clearSharedList() {
		for (Todo todo : todos.findAll()) {
			todos.delete(todo.id());
		}
	}

	@Test
	void getReturnsAnEmptyArrayInitially() throws Exception {
		mockMvc.perform(get("/api/todos"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void createReturnsTrimmedTaskAndRejectsBlankAndDuplicate() throws Exception {
		mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
				.content("{\"text\":\"  Read a book  \"}"))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("\"text\":\"Read a book\"")));

		mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
				.content("{\"text\":\"   \"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("\"code\":\"INVALID_TODO\"")));
		mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON).content("null"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("\"code\":\"MALFORMED_REQUEST\"")));

		mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
				.content("{\"text\":\"READ A BOOK\"}"))
				.andExpect(status().isConflict())
				.andExpect(content().string(containsString("\"code\":\"DUPLICATE_TODO\"")));
	}

	@Test
	void malformedBodyUsesStableClientError() throws Exception {
		mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON).content("{"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("\"code\":\"MALFORMED_REQUEST\"")));
	}

	@Test
	void updateIsReversibleAndRejectsMissingCompletionOrUnknownId() throws Exception {
		Todo created = todos.create("Toggle this");
		String path = "/api/todos/" + created.id();

		mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content("{\"completed\":true}"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"completed\":true")));
		mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content("{\"completed\":false}"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"completed\":false")));
		mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("\"code\":\"INVALID_COMPLETION\"")));
		mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content("null"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("\"code\":\"MALFORMED_REQUEST\"")));
		mockMvc.perform(put("/api/todos/missing").contentType(MediaType.APPLICATION_JSON)
				.content("{\"completed\":true}"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("\"code\":\"TODO_NOT_FOUND\"")));
	}

	@Test
	void deleteReturnsNoContentAndUnknownIdsReturnNotFound() throws Exception {
		Todo created = todos.create("Remove this");
		mockMvc.perform(delete("/api/todos/" + created.id()))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));
		mockMvc.perform(delete("/api/todos/missing"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("\"code\":\"TODO_NOT_FOUND\"")));
	}
}