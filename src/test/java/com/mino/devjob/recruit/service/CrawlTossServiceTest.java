package com.mino.devjob.recruit.service;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

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

	@Test
	void crawl() throws Exception {
//		Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriMock);
//		Mockito.when(requestHeadersUriMock.uri(Mockito.any(Function.class))).thenReturn(requestHeadersMock);
//		Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
//		Mockito.when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just(str));

		// TODO api https://toss.im/careers/api/greenhouse/jobs
		// absolute_url
		// internal_job_id
		// 4112432003 -> 고용 형태
		// 4169410003 -> 자회사
		// 4168924003 -> Job Category
		// 4194643003 -> 키워

		Flux<Recruit> tossRecruitList = crawlTossService.crawl();

		StepVerifier.create(tossRecruitList)
			.expectNextCount(2)
			.verifyComplete();
	}
}
