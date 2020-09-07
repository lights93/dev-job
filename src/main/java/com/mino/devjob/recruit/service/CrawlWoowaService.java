package com.mino.devjob.recruit.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.dto.WoowaRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service("WOOWA")
@Slf4j
public class CrawlWoowaService implements CrawlService {
	private final WebClient webClient;

	@Override
	public Flux<Recruit> crawl() {
		return getWoowaRecruits()
			.map(WoowaRecruitDto::toRecruit);
	}

	private Flux<WoowaRecruitDto> getWoowaRecruits() {
		return webClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.scheme("https")
				.host("woowahan.com")
				.pathSegment("jobapi", "jobs", "list")
				.queryParam("searchword", "")
				.queryParam("cc", "244001")
				.build())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.retrieve()
			.bodyToFlux(WoowaRecruitDto.class)
			.onErrorResume(error -> {
				log.error("get woowa error!!", error);
				return Flux.empty();
			});
	}
}
