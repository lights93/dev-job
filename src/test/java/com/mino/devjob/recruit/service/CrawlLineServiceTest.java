package com.mino.devjob.recruit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlLineServiceTest {

	@InjectMocks
	private CrawlLineService crawlLineService;

	@Test
	void crawl() {
		Flux<Recruit> lineRecruits = crawlLineService.crawl();

		StepVerifier.create(lineRecruits)
			.thenCancel()
			.verify();
	}
}