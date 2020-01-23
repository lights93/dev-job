package com.mino.devjob.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.model.Recruit;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class CrawlLineServiceTest {

	@InjectMocks
	private CrawlLineService crawlLineService;

	@Test
	void crawl() {
		Flux<Recruit> lineRecruits = crawlLineService.crawl();

		//TODO 뭐를 해야하지..
	}
}