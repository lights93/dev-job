package com.mino.devjob.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.mino.devjob.model.Recruit;
import com.mino.devjob.repository.RecruitRepository;
import com.mino.devjob.type.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service("LINE")
@RequiredArgsConstructor
@Slf4j
public class CrawlLineService implements CrawlService {
	private static final String LINE_RECRUIT_URL = "https://recruit.linepluscorp.com";
	private static final Pattern SQUARE_BRACKETS_PATTERN = Pattern.compile("\\[(.*?)\\]");
	private static final Pattern NUMBER_PATTERN = Pattern.compile(".*\\d.*");

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	@Override
	public Flux<Recruit> crawl() {
		return Mono.fromCallable(this::getLineDocument)
			.subscribeOn(Schedulers.elastic())
			.map(document -> document.select(".jobs_table tbody tr"))
			.flatMapMany(Flux::fromIterable)
			.map(row -> row.select("td"))
			.map(this::buildLineRecruit);
	}

	private Document getLineDocument() {
		try {
			return Jsoup.connect(LINE_RECRUIT_URL + "/lineplus/career/list")
				.method(Connection.Method.GET)
				.data("classId", "148")
				.execute()
				.parse();
		} catch (IOException e) {
			log.debug("line parse error ", e);
			throw new RuntimeException();
		}
	}

	private Recruit buildLineRecruit(Elements tds) {
		Element titleEl = tds.get(1);
		String link = LINE_RECRUIT_URL + titleEl.select("a").attr("href").trim();

		int startIdx = link.indexOf("/detail/") + 8;
		int endIdx = link.indexOf("?classId");

		int index = Integer.parseInt(link.substring(startIdx, endIdx));

		String title = titleEl.text().trim();

		String jobType = tds.get(3).text().trim();
		String allTerm = tds.get(4).text().trim();

		String endTerm = allTerm.substring(allTerm.lastIndexOf("~") + 1).trim();

		LocalDate end = LocalDate.of(2999, 12, 31);
		if (NUMBER_PATTERN.matcher(endTerm).matches()) { // 숫자 포함 (ex 2020.02.10)
			end = LocalDate.parse(endTerm, DATE_FORMATTER);
		}

		Matcher matcher = SQUARE_BRACKETS_PATTERN.matcher(title);
		String companyType = "";

		if (matcher.find()) {
			companyType = matcher.group(1);
		}

		return Recruit.builder()
			.index(index)
			.company(CompanyType.LINE.name())
			.title(title)
			.link(link)
			.jobType(jobType)
			.term(end)
			.companyType(companyType)
			.tags("")
			.build();
	}
}
