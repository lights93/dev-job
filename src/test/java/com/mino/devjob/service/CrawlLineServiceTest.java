package com.mino.devjob.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.model.Recruit;
import com.mino.devjob.repository.RecruitRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlLineServiceTest {

	@InjectMocks
	private CrawlLineService crawlLineService;

	@Mock
	private RecruitRepository recruitRepository;

	@Test
	void crawl() {
		Recruit recruit = Recruit.builder().title("title").build();

		Mockito.when(recruitRepository.existsByIndexAndCompany(Mockito.anyLong(), Mockito.anyString()))
			.thenReturn(Mono.just(false));

		Mockito.when(recruitRepository.save(Mockito.any(Recruit.class)))
			.thenReturn(Mono.just(recruit));

		Flux<Recruit> lineRecruits = crawlLineService.crawl();

		StepVerifier.create(lineRecruits)
			.expectNext(recruit)
			.thenCancel()
			.verify();

		Mockito.when(recruitRepository.existsByIndexAndCompany(Mockito.anyLong(), Mockito.anyString()))
			.thenReturn(Mono.just(true));

		lineRecruits = crawlLineService.crawl();

		StepVerifier.create(lineRecruits)
			.verifyComplete();
	}
}