package com.mino.devjob.book.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mino.devjob.book.service.BookService;
import com.mino.devjob.book.service.CrawlYes24Service;

import lombok.RequiredArgsConstructor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class BookSchedule {
	private static final Scheduler SCHEDULER = Schedulers.boundedElastic();
	private static final int ONE_HOUR_IN_SECONDS = 60 * 60 * 1000;

	private final BookService bookService;
	private final CrawlYes24Service crawlYes24Service;

	@Scheduled(fixedDelay = ONE_HOUR_IN_SECONDS) // 1 hour
	public void saveBooks() {
		bookService.saveAll(crawlYes24Service.crawl())
			.subscribeOn(SCHEDULER)
			.subscribe();
	}

}
