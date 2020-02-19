package com.mino.devjob.recruit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlNaverServiceTest {
	@InjectMocks
	private CrawlNaverService crawlNaver;

	@Test
	void crawl() {
		Flux<Recruit> naverRecruitList = crawlNaver.crawl();

		StepVerifier.create(naverRecruitList)
			.thenCancel()
			.verify();
	}
}