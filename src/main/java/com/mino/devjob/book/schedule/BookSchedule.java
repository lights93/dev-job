package com.mino.devjob.book.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.service.BookService;
import com.mino.devjob.book.service.CrawlYes24Service;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookSchedule {
	private final BookService bookService;
	private final CrawlYes24Service crawlYes24Service;

	@Scheduled(fixedDelay = 60 * 60 * 1000) // 1 hour
	public void saveBooks() {
		crawlYes24Service.crawl()
			.distinct(Book::getId)
			.collectList()
			.flatMapMany(bookService::saveAll)
			.collectList()
			.block();
	}

}
