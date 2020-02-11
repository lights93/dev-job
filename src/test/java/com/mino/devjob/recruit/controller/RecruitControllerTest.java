package com.mino.devjob.recruit.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.service.RecruitService;
import com.mino.devjob.recruit.type.CompanyType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest(RecruitController.class)
class RecruitControllerTest {
	@MockBean
	private RecruitService recruitService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getRecruits() {
		Recruit recruit = Recruit.builder().title("title").build();

		Flux<Recruit> recruitFlux = Flux.just(recruit);

		Mockito.when(recruitService.getRecruits(Mockito.anyString()))
			.thenReturn(recruitFlux);

		Flux<Recruit> responseBody = webTestClient.get()
			.uri("/api/recruits/" + CompanyType.KAKAO.name())
			.exchange()
			.expectStatus().isOk()
			.returnResult(Recruit.class)
			.getResponseBody();

		StepVerifier.create(responseBody)
			.expectNext(recruit)
			.verifyComplete();
	}

	@Test
	void getAllCount() {
		Mockito.when(recruitService.getAllCount())
			.thenReturn(Mono.just(10L));

		webTestClient.get()
			.uri("/api/recruits/count")
			.exchange()
			.expectStatus().isOk()
			.expectBody(Long.class)
			.isEqualTo(10L);
	}

	@Test
	void putRecruit() {
		Recruit recruit = Recruit.builder().favorite(false).build();
		Recruit recruit2 = Recruit.builder().favorite(true).build();

		Mockito.when(recruitService.update(Mockito.eq(recruit)))
			.thenReturn(Mono.just(recruit2));

		webTestClient.put()
			.uri("/api/recruits/")
			.body(Mono.just(recruit), Recruit.class)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Recruit.class)
			.isEqualTo(recruit2);
	}
}