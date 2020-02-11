package com.mino.devjob.service;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.dto.WoowaRecruitDto;
import com.mino.devjob.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlWoowaServiceTest {
	@InjectMocks
	private CrawlWoowaService crawlWoowaService;

	@Mock
	private ObjectMapper mapper;

	@Test
	void crawl() throws IOException {
		WoowaRecruitDto woowaRecruitDto = WoowaRecruitDto.builder()
			.jobTitle("title")
			.careerName("경력")
			.eDate(LocalDate.MAX)
			.businessName("개발")
			.build();
		WoowaRecruitDto[] woowaRecruitDtos = {woowaRecruitDto};

		Recruit recruit = woowaRecruitDto.toRecruit();

		Mockito.when(mapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(woowaRecruitDtos);

		Flux<Recruit> woowaRecruitFlux = crawlWoowaService.crawl();

		StepVerifier.create(woowaRecruitFlux)
			.expectNext(recruit)
			.verifyComplete();
	}
}