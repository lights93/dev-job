package com.mino.devjob.book.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.book.model.Book;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlYes24ServiceTest {
	@InjectMocks
	private CrawlYes24Service crawlYes24Service;

	@Test
	void crawl() {
		Flux<Book> bookFlux = crawlYes24Service.crawl();

		StepVerifier.create(bookFlux)
			.expectNextCount(400)
			.verifyComplete();

		bookFlux.toStream()
			.forEach(System.out::println);
	}
}