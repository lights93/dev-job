package com.mino.devjob.recruit.service;

import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.mino.devjob.recruit.dto.WoowaRecruitDto;
import com.mino.devjob.recruit.model.Recruit;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlWoowaServiceTest {
	@InjectMocks
	private CrawlWoowaService crawlWoowaService;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private WebClient webClientMock;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private WebClient.RequestHeadersSpec requestHeadersMock;

	@Test
	void crawl() {
		WoowaRecruitDto woowaRecruitDto = WoowaRecruitDto.builder().build();
		WoowaRecruitDto woowaRecruitDto2 = WoowaRecruitDto.builder().build();

		when(webClientMock.get()
			.uri(ArgumentMatchers.<Function<UriBuilder, URI>>any())
			.header(anyString(), anyString())
		).thenReturn(requestHeadersMock);

		when(requestHeadersMock.retrieve().bodyToFlux(WoowaRecruitDto.class))
			.thenReturn(Flux.just(woowaRecruitDto, woowaRecruitDto2));

		Flux<Recruit> woowaRecruitFlux = crawlWoowaService.crawl();

		StepVerifier.create(woowaRecruitFlux)
			.expectNextCount(2)
			.verifyComplete();
	}
}