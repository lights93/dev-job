package com.mino.devjob.recruit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlCoupangServiceTest {
	@InjectMocks
	private CrawlCoupangService crawlCoupangService;

	@Test
	void crawl() {
		Flux<Recruit> crawl = crawlCoupangService.crawl();

		StepVerifier.create(crawl)
			.thenCancel()
			.verify();
	}
}