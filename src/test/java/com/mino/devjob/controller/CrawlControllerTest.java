package com.mino.devjob.controller;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mino.devjob.model.Recruit;
import com.mino.devjob.service.CrawlKakaoService;
import com.mino.devjob.service.CrawlService;
import com.mino.devjob.type.CompanyType;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CrawlController.class)
class CrawlControllerTest {
	@MockBean
	private Map<CompanyType, CrawlService> crawlServiceMap;

	@Mock
	private CrawlService crawlService = new CrawlKakaoService();

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void crawl_SUCCESS() {
		Recruit recruit = Recruit.builder().title("title").build();
		Flux<Recruit> recruits = Flux.just(recruit);

		Mockito.when(crawlServiceMap.get(CompanyType.KAKAO))
			.thenReturn(crawlService);

		Mockito.when(crawlService.crawl())
			.thenReturn(recruits);

		Flux<Recruit> responseBody = webTestClient.get()
			.uri("/api/crawl/" + CompanyType.KAKAO.name())
			.exchange()
			.expectStatus().isOk()
			.returnResult(Recruit.class)
			.getResponseBody();

		StepVerifier.create(responseBody)
			.expectNext(recruit)
			.verifyComplete();
	}

	@Test
	void crawl_SUCCESS_ALL() {
		Recruit recruit = Recruit.builder().title("title").build();
		Flux<Recruit> recruits = Flux.just(recruit);

		Mockito.when(crawlServiceMap.values())
			.thenReturn(List.of(crawlService));

		Mockito.when(crawlService.crawl())
			.thenReturn(recruits);

		Flux<Recruit> responseBody = webTestClient.get()
			.uri("/api/crawl/" + CompanyType.ALL.name())
			.exchange()
			.expectStatus().isOk()
			.returnResult(Recruit.class)
			.getResponseBody();

		StepVerifier.create(responseBody)
			.expectNext(recruit)
			.verifyComplete();
	}

	@Test
	void crawl_FAIL() {
		String company = "FAIL";

		webTestClient.get()
			.uri("/api/crawl/" + company)
			.exchange()
			.expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	void getCompaines() {
		Flux<CompanyType> responseBody = webTestClient.get()
			.uri("/api/companies/")
			.exchange()
			.expectStatus().isOk()
			.returnResult(CompanyType.class)
			.getResponseBody();

		StepVerifier.create(responseBody)
			.expectNext(CompanyType.ALL)
			.expectNextCount(CompanyType.values().length - 1)
			.verifyComplete();
	}
}