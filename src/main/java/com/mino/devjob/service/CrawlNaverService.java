package com.mino.devjob.service;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.dto.NaverRecruitDto;
import com.mino.devjob.model.NaverRecruit;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service("naver")
@RequiredArgsConstructor
public class CrawlNaverService implements CrawlService<NaverRecruit> {
	private final ObjectMapper mapper;

	public Flux<NaverRecruit> crawl() throws IOException {
		String body = Jsoup.connect("https://recruit.navercorp.com/naver/job/listJson")
			.method(Connection.Method.POST)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.data("classNm", "developer")
			.data("startNum", "1")
			.data("endNum", "30")
			.ignoreContentType(true)
			.execute()
			.body();

		return Flux.fromArray(mapper.readValue(body, NaverRecruitDto[].class))
			.map(NaverRecruitDto::toNaverRecruit);
	}
}
