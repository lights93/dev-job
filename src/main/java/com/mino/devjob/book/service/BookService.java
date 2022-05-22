package com.mino.devjob.book.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookService {
	private static final int NOT_FAVORITE = 0;

	private final BookRepository bookRepository;

	public Mono<List<Book>> saveAll(Flux<Book> bookFlux) {
		return bookFlux
			.distinct(Book::getId)
			.filterWhen(book -> bookRepository.existsByIdAndFavoriteIsNot(book.getId(), NOT_FAVORITE).map(b -> !b))
			.collectList()
			.flatMapMany(bookRepository::saveAll)
			.collectList();
	}

	public Flux<Book> getAll() {
		return bookRepository.findAll();
	}

	public Mono<Long> getAllCount() {
		return bookRepository.count();
	}

	public Mono<Book> update(Book book) {
		return bookRepository.save(book);
	}
}
