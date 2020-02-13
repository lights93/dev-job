package com.mino.devjob.recruit.service;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.recruit.dto.WoowaRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service("WOOWA")
@RequiredArgsConstructor
@Slf4j
public class CrawlWoowaService implements CrawlService {
	private final ObjectMapper mapper;

	@Override
	public Flux<Recruit> crawl() {
		return Mono.fromCallable(this::getWoowaRecruits)
			.subscribeOn(Schedulers.elastic())
			.filter(Optional::isPresent)
			.map(Optional::get)
			.flatMapMany(Flux::fromArray)
			.map(WoowaRecruitDto::toRecruit);
	}

	private Optional<WoowaRecruitDto[]> getWoowaRecruits() {
		try {
			String body = Jsoup.connect("https://www.woowahan.com/jobapi/jobs/list?searchword=&cc=244001")
				.method(Connection.Method.GET)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.ignoreContentType(true)
				.execute()
				.body();

			return Optional.ofNullable(mapper.readValue(body, WoowaRecruitDto[].class));
		} catch (IOException e) {
			log.error("woowa parse error ", e);
			return Optional.empty();
		}

	}
}
