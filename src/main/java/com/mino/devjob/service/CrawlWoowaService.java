package com.mino.devjob.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.dto.WoowaRecruitDto;
import com.mino.devjob.model.Recruit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;

@Service("WOOWA")
@RequiredArgsConstructor
public class CrawlWoowaService implements CrawlService {
	private final ObjectMapper mapper;

	@SneakyThrows
	@Override
	public Flux<Recruit> crawl() {
		String body = Jsoup.connect("https://www.woowahan.com/jobapi/jobs/list?searchword=&cc=244001")
			.method(Connection.Method.GET)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.ignoreContentType(true)
			.execute()
			.body();

		return Flux.fromArray(mapper.readValue(body, WoowaRecruitDto[].class))
			.map(WoowaRecruitDto::toRecruit);
	}
}
