package com.mino.devjob.book.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.service.BookService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;

	@GetMapping
	@ApiOperation(value = "책 전체 DB 조회")
	public Flux<Book> getBooks() {
		return bookService.getAll();
	}

	@GetMapping("count")
	@ApiOperation(value = "책 갯수 DB 조회")
	public Mono<Long> getBooksCount() {
		return bookService.getAllCount();
	}

	@PutMapping
	@ApiOperation(value = "책 DB 수정")
	public Mono<Book> putBook(@RequestBody Book book) {
		return bookService.update(book);
	}
}
