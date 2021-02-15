package com.mino.devjob.recruit.service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.recruit.dto.TossRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CrawlTossServiceTest {
	private CrawlTossService crawlTossService;

	@Mock
	private WebClient webClientMock;
	@Mock
	private WebClient.RequestHeadersSpec requestHeadersMock;
	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
	@Mock
	private WebClient.ResponseSpec responseMock;
	@Mock
	private WebClient.Builder builderMock;

	@Mock
	private ObjectMapper mapper;

	@Test
	void crawl() throws Exception {
		List<TossRecruitDto.Job> jobs = List.of(TossRecruitDto.Job.builder()
			.metadata(List.of(TossRecruitDto.Metadata.builder()
				.id(4168924003L)
				.value("Engineering")
				.build()))
			.build());

		TossRecruitDto tossRecruitDto = TossRecruitDto.builder()
			.jobs(jobs)
			.build();

		Mockito.when(webClientMock.mutate()).thenReturn(builderMock);
		Mockito.when(builderMock.exchangeStrategies(Mockito.any(ExchangeStrategies.class))).thenReturn(builderMock);
		Mockito.when(builderMock.build()).thenReturn(webClientMock);
		Mockito.when(mapper.copy()).thenReturn(new ObjectMapper());

		Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriMock);
		Mockito.when(requestHeadersUriMock.uri(Mockito.eq("https://toss.im/careers/api/greenhouse/jobs"))).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
		Mockito.when(responseMock.bodyToMono(TossRecruitDto.class)).thenReturn(Mono.just(tossRecruitDto));

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
