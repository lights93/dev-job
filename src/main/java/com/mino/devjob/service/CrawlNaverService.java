package com.mino.devjob.service;

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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service("NAVER")
@RequiredArgsConstructor
@Slf4j
public class CrawlNaverService implements CrawlService {
	private final ObjectMapper mapper;
	private final RecruitRepository recruitRepository;

	@SneakyThrows
	public Flux<Recruit> crawl() {
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

		return Flux.fromArray(mapper.readValue(body, NaverRecruitDto[].class))
			.map(NaverRecruitDto::toRecruit)
			.filterWhen(r -> recruitRepository.existsByIndexAndCompany(r.getIndex(), r.getCompany())
				.map(b -> !b))
			.flatMap(recruitRepository::save);
	}
}
