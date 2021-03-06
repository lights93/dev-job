package com.mino.devjob.book.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.service.BookService;
import com.mino.devjob.book.service.CrawlYes24Service;
import lombok.RequiredArgsConstructor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class BookSchedule {
	private final BookService bookService;
	private final CrawlYes24Service crawlYes24Service;
	private static final Scheduler SCHEDULER = Schedulers.boundedElastic();

	@Scheduled(fixedDelay = 60 * 60 * 1000) // 1 hour
	public void saveBooks() {
		crawlYes24Service.crawl()
			.distinct(Book::getId)
			.filterWhen(b -> bookService.notExistsByIndexAndCompanyAndFavoriteIsNot(b, 0))
			.collectList()
			.flatMapMany(bookService::saveAll)
			.collectList()
			.subscribeOn(SCHEDULER)
			.subscribe();
	}

}
