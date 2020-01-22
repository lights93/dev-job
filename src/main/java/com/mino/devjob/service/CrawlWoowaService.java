package com.mino.devjob.service;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.devjob.dto.WoowaRecruitDto;
import com.mino.devjob.model.WoowaRecruit;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service("woowa")
@RequiredArgsConstructor
public class CrawlWoowaService implements CrawlService<WoowaRecruit> {
	private final ObjectMapper mapper;

	@Override
	public Flux<WoowaRecruit> crawl() throws IOException {
		String body = Jsoup.connect("https://www.woowahan.com/jobapi/jobs/list?searchword=&cc=244001")
			.method(Connection.Method.GET)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.ignoreContentType(true)
			.execute()
			.body();

		return Flux.fromArray(mapper.readValue(body, WoowaRecruitDto[].class))
			.map(WoowaRecruitDto::toWoowaRecruit);
	}
}
