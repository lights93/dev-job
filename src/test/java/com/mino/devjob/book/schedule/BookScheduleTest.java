package com.mino.devjob.book.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.service.BookService;
import com.mino.devjob.book.service.CrawlYes24Service;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class BookScheduleTest {
	@InjectMocks
	private BookSchedule bookSchedule;

	@Mock
	private BookService bookService;

	@Mock
	private CrawlYes24Service crawlYes24Service;

	@Test
	void saveBooks() {
		Book book = Book.builder().id(1L).build();
		Book book2 = Book.builder().id(2L).build();

		Flux<Book> bookFlux = Flux.just(book);
		Flux<Book> bookFlux2 = Flux.just(book2);

		Mockito.when(crawlYes24Service.crawl())
			.thenReturn(bookFlux);

		Mockito.when(bookService.saveAll(Mockito.eq(bookFlux.collectList().block())))
			.thenReturn(bookFlux2);

		bookSchedule.saveBooks();

		Mockito.verify(crawlYes24Service, Mockito.times(1))
			.crawl();

		Mockito.verify(bookService, Mockito.times(1))
			.saveAll(Mockito.eq(bookFlux.collectList().block()));
	}
}