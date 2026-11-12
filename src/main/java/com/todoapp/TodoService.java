package com.todoapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

	private final List<Todo> todos = new ArrayList<>();

	public synchronized List<Todo> findAll() {
		return List.copyOf(todos);
	}

	public synchronized Todo create(String text) {
		if (text == null || text.strip().isEmpty()) {
			throw new TodoException("INVALID_TODO", "Task text must not be blank.", HttpStatus.BAD_REQUEST);
		}

		String normalizedText = text.strip();
		String duplicateKey = normalizedText.toLowerCase(Locale.ROOT);
		boolean duplicate = todos.stream()
				.anyMatch(todo -> todo.text().toLowerCase(Locale.ROOT).equals(duplicateKey));
		if (duplicate) {
			throw new TodoException("DUPLICATE_TODO", "A task with this text already exists.", HttpStatus.CONFLICT);
		}

		Todo todo = new Todo(UUID.randomUUID().toString(), normalizedText, false);
		todos.add(todo);
		return todo;
	}

	public synchronized Todo updateCompletion(String id, Boolean completed) {
		if (completed == null) {
			throw new TodoException("INVALID_COMPLETION", "Completion value is required.", HttpStatus.BAD_REQUEST);
		}

		for (int index = 0; index < todos.size(); index++) {
			Todo current = todos.get(index);
			if (current.id().equals(id)) {
				Todo updated = new Todo(current.id(), current.text(), completed);
				todos.set(index, updated);
				return updated;
			}
		}
		throw notFound();
	}

	public synchronized void delete(String id) {
		boolean removed = todos.removeIf(todo -> todo.id().equals(id));
		if (!removed) {
			throw notFound();
		}
	}

	private TodoException notFound() {
		return new TodoException("TODO_NOT_FOUND", "Task was not found.", HttpStatus.NOT_FOUND);
	}
}