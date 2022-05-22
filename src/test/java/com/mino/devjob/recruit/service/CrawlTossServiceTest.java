package com.mino.devjob.recruit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.recruit.dto.TossRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlTossServiceTest {
	private CrawlTossService crawlTossService;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private WebClient webClientMock;

	@Mock
	private ObjectMapper mapper;

	@Test
	void crawl() {
		List<TossRecruitDto.Job> jobs = List.of(TossRecruitDto.Job.builder()
			.metadata(List.of(TossRecruitDto.Metadata.builder()
				.id(4168924003L)
				.value("Engineering")
				.build()))
			.build());

		TossRecruitDto tossRecruitDto = TossRecruitDto.builder()
			.jobs(jobs)
			.build();

		when(webClientMock.mutate()
			.exchangeStrategies(any(ExchangeStrategies.class))
			.build()).thenReturn(webClientMock);

		Mockito.when(mapper.copy()).thenReturn(new ObjectMapper());

		when(webClientMock.get()
			.uri(anyString())
			.retrieve().bodyToMono(ArgumentMatchers.<Class<TossRecruitDto>>any()))
			.thenReturn(Mono.just(tossRecruitDto));

		// TODO api https://toss.im/careers/api/greenhouse/jobs
		// absolute_url
		// internal_job_id
		// 4112432003 -> 고용 형태
		// 4169410003 -> 자회사
		// 4168924003 -> Job Category
		// 4194643003 -> 키워

		crawlTossService = new CrawlTossService(webClientMock, mapper);

		Flux<Recruit> tossRecruitList = crawlTossService.crawl();
		//
		StepVerifier.create(tossRecruitList)
			.expectNextCount(1)
			.verifyComplete();
	}
}
