package com.mino.devjob.recruit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlDaangnServiceTest {
	@InjectMocks
	private CrawlDaangnService crawlDaangnService;

	@Test
	void crawl() {
		Flux<Recruit> lineRecruits = crawlDaangnService.crawl();

		StepVerifier.create(lineRecruits)
			.thenCancel()
			.verify();
	}
}