package com.mino.devjob.service;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.dto.NaverRecruitDto;
import com.mino.devjob.model.Recruit;
import com.mino.devjob.repository.RecruitRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CrawlNaverServiceTest {
	@InjectMocks
	private CrawlNaverService crawlNaver;

	@Mock
	private ObjectMapper mapper;

	@Mock
	private RecruitRepository recruitRepository;

	@Test
	void crawl() throws IOException {
		NaverRecruitDto naverRecruitDto = NaverRecruitDto.builder()
			.jobNm("jobNm")
			.annoId(1)
			.entTypeCd("001")
			.endYmd("20200101")
			.sysCompanyCd("NB")
			.jobKeyword("# 벡엔드 개발")
			.build();
		NaverRecruitDto[] naverRecruitDtos = {naverRecruitDto};

		Recruit recruit = naverRecruitDto.toRecruit();

		Mockito.when(mapper.readValue(Mockito.anyString(), Mockito.any(Class.class)))
			.thenReturn(naverRecruitDtos);

		Mockito.when(recruitRepository.existsByIndexAndCompany(Mockito.anyLong(), Mockito.anyString()))
			.thenReturn(Mono.just(false));

		Mockito.when(recruitRepository.save(Mockito.any(Recruit.class)))
			.thenReturn(Mono.just(recruit));

		Flux<Recruit> naverRecruitList = crawlNaver.crawl();

		StepVerifier.create(naverRecruitList)
			.expectNext(recruit)
			.verifyComplete();

		Mockito.when(recruitRepository.existsByIndexAndCompany(Mockito.anyLong(), Mockito.anyString()))
			.thenReturn(Mono.just(true));

		naverRecruitList = crawlNaver.crawl();

		StepVerifier.create(naverRecruitList)
			.verifyComplete();

	}
}