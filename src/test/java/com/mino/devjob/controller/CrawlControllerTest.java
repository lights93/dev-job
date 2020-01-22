package com.mino.devjob.controller;

import java.io.IOException;
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

import com.mino.devjob.model.KakaoRecruit;
import com.mino.devjob.service.CrawlKakaoService;
import com.mino.devjob.service.CrawlService;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CrawlController.class)
class CrawlControllerTest {
	@MockBean
	private Map<String, CrawlService> crawlServiceMap;

	@Mock
	private CrawlService crawlService = new CrawlKakaoService();

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void crawl_SUCCESS() throws IOException {
		KakaoRecruit kakaoRecruit = KakaoRecruit.builder().title("title").build();
		Flux<KakaoRecruit> kakaoRecruits = Flux.just(kakaoRecruit);

		String company = "test";

		Mockito.when(crawlServiceMap.containsKey(company))
			.thenReturn(true);

		Mockito.when(crawlServiceMap.get(company))
			.thenReturn(crawlService);

		Mockito.when(crawlService.crawl())
			.thenReturn(kakaoRecruits);

		Flux<KakaoRecruit> responseBody = webTestClient.get()
			.uri("/api/crawl/" + company)
			.exchange()
			.expectStatus().isOk()
			.returnResult(KakaoRecruit.class)
			.getResponseBody();

		StepVerifier.create(responseBody)
			.expectNext(kakaoRecruit)
			.verifyComplete();
	}

	@Test
	void crawl_FAIL() {
		String company = "test";

		Mockito.when(crawlServiceMap.containsKey(company))
			.thenReturn(false);

		webTestClient.get()
			.uri("/api/crawl/" + company)
			.exchange()
			.expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}