package com.mino.devjob.recruit.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.dto.NaverRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("NAVER")
@Slf4j
public class CrawlNaverService implements CrawlService {
	private final WebClient webClient = WebClient.create("https://recruit.navercorp.com");

	@Override
	public Flux<Recruit> crawl() {
		return getNaverRecruits()
			.map(NaverRecruitDto::toRecruit);
	}

	private Flux<NaverRecruitDto> getNaverRecruits() {
		return webClient
			.post()
			.uri(uriBuilder -> uriBuilder
				.pathSegment("naver", "job", "listJson")
				.queryParam("classNm", "developer")
				.queryParam("startNum", "1")
				.queryParam("endNum", "300")
				.build())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.retrieve()
			.bodyToFlux(NaverRecruitDto.class)
			.onErrorResume(error -> Mono.empty());
	}
}
