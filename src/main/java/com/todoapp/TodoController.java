package com.todoapp;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

	private final TodoService todos;

	public TodoController(TodoService todos) {
		this.todos = todos;
	}

	@GetMapping
	public List<Todo> findAll() {
		return todos.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Todo create(@RequestBody TodoRequest request) {
		return todos.create(request == null ? null : request.text());
	}

	@PutMapping("/{id}")
	public Todo update(@PathVariable String id, @RequestBody TodoRequest request) {
		return todos.updateCompletion(id, request == null ? null : request.completed());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String id) {
		todos.delete(id);
	}
}