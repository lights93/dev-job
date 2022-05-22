package com.mino.devjob.book.service;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.repository.BookRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	@InjectMocks
	private BookService bookService;

	@Mock
	private BookRepository bookRepository;

	@Test
	void saveAll() {
		Book book = Book.builder().id(1L).build();

		Flux<Book> bookFlux = Flux.just(book);
		when(bookRepository.saveAll(anyList()))
			.thenReturn(bookFlux);

		when(bookRepository.existsByIdAndFavoriteIsNot(any(), anyInt()))
			.thenReturn(Mono.just(true));

		StepVerifier.create(bookService.saveAll(bookFlux))
			.expectNext(List.of(book))
			.verifyComplete();
	}

	@Test
	void getAll() {
		Book book = Book.builder().id(1L).build();

		Flux<Book> bookFlux = Flux.just(book);
		when(bookRepository.findAll())
			.thenReturn(bookFlux);

		StepVerifier.create(bookService.getAll())
			.expectNext(book)
			.verifyComplete();
	}

	@Test
	void getAllCount() {
		when(bookRepository.count())
			.thenReturn(Mono.just(100L));

		StepVerifier.create(bookService.getAllCount())
			.expectNext(100L)
			.verifyComplete();
	}

	@Test
	void update() {
		Book book = Book.builder().id(1L).build();

		Mono<Book> bookMono = Mono.just(book);
		when(bookRepository.save(book))
			.thenReturn(bookMono);

		StepVerifier.create(bookService.update(book))
			.expectNext(book)
			.verifyComplete();
	}
}