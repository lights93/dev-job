package com.mino.devjob.recruit.service;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.dto.NaverRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlNaverServiceTest {
	private CrawlNaverService crawlNaver;
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
		NaverRecruitDto dto1 = NaverRecruitDto.builder().entTypeCd("001").endYmd("20200930").build();
		NaverRecruitDto dto2 = NaverRecruitDto.builder().entTypeCd("001").endYmd("20201030").build();

		Mockito.when(webClientMock.mutate()).thenReturn(builderMock);
		Mockito.when(builderMock.baseUrl("https://recruit.navercorp.com")).thenReturn(builderMock);
		Mockito.when(builderMock.clientConnector(Mockito.any(ClientHttpConnector.class))).thenReturn(builderMock);
		Mockito.when(builderMock.build()).thenReturn(webClientMock);

		crawlNaver = new CrawlNaverService(webClientMock);

		Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriMock);
		Mockito.when(requestHeadersUriMock.uri(Mockito.any(Function.class))).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
			.thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
		Mockito.when(responseMock.bodyToFlux(NaverRecruitDto.class)).thenReturn(Flux.just(dto1, dto2));

		Flux<Recruit> naverRecruitList = crawlNaver.crawl();

		StepVerifier.create(naverRecruitList)
			.expectNextCount(2)
			.verifyComplete();
	}
}