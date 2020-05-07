package com.mino.devjob.book.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.service.BookService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BookController.class)
class BookControllerTest {
	@MockBean
	private BookService bookService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getBooks() {
		Book book = Book.builder().id(1L).build();

		Flux<Book> bookFlux = Flux.just(book);
		Mockito.when(bookService.getAll())
			.thenReturn(bookFlux);

		Flux<Book> responseBody = webTestClient.get()
			.uri("/api/books")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Book.class)
			.getResponseBody();

		StepVerifier.create(responseBody)
			.expectNext(book)
			.verifyComplete();
	}

	@Test
	void getBooksCount() {
		Mockito.when(bookService.getAllCount())
			.thenReturn(Mono.just(10L));

		webTestClient.get()
			.uri("/api/books/count")
			.exchange()
			.expectStatus().isOk()
			.expectBody(Long.class)
			.isEqualTo(10L);
	}

	@Test
	void putBook() {
		Book book = Book.builder().favorite(1).build();
		Book book2 = Book.builder().favorite(-1).build();

		Mockito.when(bookService.update(Mockito.eq(book)))
			.thenReturn(Mono.just(book2));

		webTestClient.put()
			.uri("/api/books/")
			.body(Mono.just(book), Book.class)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class)
			.isEqualTo(book2);
	}
}