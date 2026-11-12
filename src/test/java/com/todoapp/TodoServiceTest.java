package com.todoapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

class TodoServiceTest {

	private final TodoService todos = new TodoService();

	@Test
	void trimsTextRejectsBlankAndCaseInsensitiveDuplicates() {
		Todo created = todos.create("  Write tests  ");

		assertEquals("Write tests", created.text());
		assertEquals("INVALID_TODO", assertThrows(TodoException.class, () -> todos.create(" \t ")).code());
		assertEquals("DUPLICATE_TODO", assertThrows(TodoException.class, () -> todos.create("write TESTS")).code());
	}

	@Test
	void duplicateComparisonDoesNotDependOnDefaultLocale() {
		Locale originalLocale = Locale.getDefault();
		try {
			Locale.setDefault(Locale.forLanguageTag("tr-TR"));
			todos.create("KIND");
			assertEquals("DUPLICATE_TODO", assertThrows(TodoException.class, () -> todos.create("kind")).code());
		} finally {
			Locale.setDefault(originalLocale);
		}
	}

	@Test
	void completionCanBeReversedAndUnknownIdsFail() {
		Todo created = todos.create("Review plan");

		assertTrue(todos.updateCompletion(created.id(), true).completed());
		assertFalse(todos.updateCompletion(created.id(), false).completed());
		assertEquals("TODO_NOT_FOUND", assertThrows(TodoException.class,
				() -> todos.updateCompletion("missing", true)).code());
		assertEquals("TODO_NOT_FOUND", assertThrows(TodoException.class,
				() -> todos.delete("missing")).code());
	}

	@Test
	void readsAreDefensiveAndDeleteRemovesTask() {
		Todo created = todos.create("Keep list safe");
		List<Todo> snapshot = todos.findAll();

		assertThrows(UnsupportedOperationException.class, snapshot::clear);
		todos.delete(created.id());
		assertEquals(List.of(), todos.findAll());
	}

	@Test
	void concurrentDuplicateSubmissionsCreateExactlyOneTask() throws Exception {
		int contenders = 12;
		CountDownLatch ready = new CountDownLatch(contenders);
		CountDownLatch start = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(contenders);
		try {
			List<Future<Boolean>> results = new ArrayList<>();
			for (int index = 0; index < contenders; index++) {
				results.add(executor.submit(() -> {
					ready.countDown();
					start.await();
					try {
						todos.create("Concurrent task");
						return true;
					} catch (TodoException exception) {
						return false;
					}
				}));
			}

			assertTrue(ready.await(5, java.util.concurrent.TimeUnit.SECONDS));
			start.countDown();
			int createdCount = 0;
			for (Future<Boolean> result : results) {
				if (result.get()) {
					createdCount++;
				}
			}
			assertEquals(1, createdCount);
			assertEquals(1, todos.findAll().size());
		} finally {
			start.countDown();
			executor.shutdownNow();
		}
	}
}