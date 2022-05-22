package com.mino.devjob.recruit.service;

import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.mino.devjob.recruit.dto.NaverRecruitDto;
import com.mino.devjob.recruit.model.Recruit;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlNaverServiceTest {
	private CrawlNaverService crawlNaver;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private WebClient webClientMock;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private WebClient.RequestHeadersSpec requestHeadersMock;

	@Test
	void crawl() {
		NaverRecruitDto dto1 = NaverRecruitDto.builder().entTypeCd("001").endYmd("20200930").build();
		NaverRecruitDto dto2 = NaverRecruitDto.builder().entTypeCd("001").endYmd("20201030").build();

		when(webClientMock.mutate()
			.baseUrl(anyString())
			.clientConnector(any())
			.build()).thenReturn(webClientMock);

		crawlNaver = new CrawlNaverService(webClientMock);

		when(webClientMock.get()
			.uri(ArgumentMatchers.<Function<UriBuilder, URI>>any())
			.header(anyString(), anyString())
		).thenReturn(requestHeadersMock);

		Mockito.when(requestHeadersMock.retrieve().bodyToFlux(NaverRecruitDto.class))
			.thenReturn(Flux.just(dto1, dto2));

		Flux<Recruit> naverRecruitList = crawlNaver.crawl();

		StepVerifier.create(naverRecruitList)
			.expectNextCount(2)
			.verifyComplete();
	}
}