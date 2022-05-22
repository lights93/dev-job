package com.mino.devjob.book.schedule;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.service.BookService;
import com.mino.devjob.book.service.CrawlYes24Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

		when(crawlYes24Service.crawl())
			.thenReturn(Flux.just(book));

		when(bookService.saveAll(any()))
			.thenReturn(Mono.just(List.of(book)));

		bookSchedule.saveBooks();

		verify(crawlYes24Service, times(1)).crawl();
		verify(bookService, times(1)).saveAll(any());
	}
}