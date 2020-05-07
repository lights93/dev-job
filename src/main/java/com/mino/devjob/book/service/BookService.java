package com.mino.devjob.book.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.repository.BookRepository;
import com.mino.devjob.recruit.model.Recruit;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public Flux<Book> saveAll(List<Book> bookList) {
		return bookRepository.saveAll(bookList);
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

//	public Flux<Book> getBooks(int favorite) {
//		return bookRepository.findAllByFavoriteIsLessThan(favorite);
//	}

	public Mono<Boolean> notExistsByIndexAndCompanyAndFavoriteIsNot(Book book, int favorite) {
		return bookRepository.existsByIdAndFavoriteIsNot(book.getId(), favorite).map(b -> !b);
	}
}
