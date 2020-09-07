package com.mino.devjob.recruit.service;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.dto.WoowaRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlWoowaServiceTest {
	@InjectMocks
	private CrawlWoowaService crawlWoowaService;
	@Mock
	private WebClient webClientMock;
	@Mock
	private WebClient.RequestHeadersSpec requestHeadersMock;
	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
	@Mock
	private WebClient.ResponseSpec responseMock;

	@Test
	void crawl() {
		WoowaRecruitDto woowaRecruitDto = WoowaRecruitDto.builder().build();
		WoowaRecruitDto woowaRecruitDto2 = WoowaRecruitDto.builder().build();

		Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriMock);
		Mockito.when(requestHeadersUriMock.uri(Mockito.any(Function.class))).thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
			.thenReturn(requestHeadersMock);
		Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
		Mockito.when(responseMock.bodyToFlux(WoowaRecruitDto.class)).thenReturn(
			Flux.just(woowaRecruitDto, woowaRecruitDto2));

		Flux<Recruit> woowaRecruitFlux = crawlWoowaService.crawl();

		StepVerifier.create(woowaRecruitFlux)
			.expectNextCount(2)
			.verifyComplete();
	}
}