package com.mino.devjob.service;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.dto.NaverRecruitDto;
import com.mino.devjob.model.Recruit;
import com.mino.devjob.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service("NAVER")
@RequiredArgsConstructor
@Slf4j
public class CrawlNaverService implements CrawlService {
	private final ObjectMapper mapper;

	@Override
	public Flux<Recruit> crawl() {
		return Mono.fromCallable(this::getNaverRecruits)
			.subscribeOn(Schedulers.elastic())
			.flatMapMany(Flux::fromArray)
			.map(NaverRecruitDto::toRecruit);
	}

	private NaverRecruitDto[] getNaverRecruits() {
		try {
			String body = Jsoup.connect("https://recruit.navercorp.com/naver/job/listJson")
				.method(Connection.Method.POST)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.data("classNm", "developer")
				.data("startNum", "1")
				.data("endNum", "300")
				.ignoreContentType(true)
				.maxBodySize(0)
				.execute()
				.body();

			return mapper.readValue(body, NaverRecruitDto[].class);
		} catch (IOException e) {
			log.debug("naver parse error ", e);
			throw new RuntimeException();
		}

	}
}
