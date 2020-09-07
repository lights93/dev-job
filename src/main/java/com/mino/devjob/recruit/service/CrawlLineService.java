package com.mino.devjob.recruit.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service("LINE")
@Slf4j
public class CrawlLineService implements CrawlService {
	private static final String LINE_RECRUIT_HOST = "recruit.linepluscorp.com";
	private static final Pattern SQUARE_BRACKETS_PATTERN = Pattern.compile("\\[(.*?)\\]");
	private static final Pattern NUMBER_PATTERN = Pattern.compile(".*\\d.*");

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	private final WebClient webClient;

	@Override
	public Flux<Recruit> crawl() {
		return getLineDocument()
			.map(document -> document.select(".jobs_table tbody tr"))
			.flatMapMany(Flux::fromIterable)
			.map(row -> row.select("td"))
			.map(this::buildLineRecruit);
	}

	private Mono<Document> getLineDocument() {
		WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = webClient.get();
		return webClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.scheme("https")
				.host(LINE_RECRUIT_HOST)
				.pathSegment("lineplus", "career", "list")
				.queryParam("classId", "148")
				.build())
			.retrieve()
			.bodyToMono(String.class)
			.map(Jsoup::parse)
			.onErrorResume(error -> {
				log.error("get line error!!", error);
				return Mono.empty();
			});
	}

	private Recruit buildLineRecruit(Elements tds) {
		Element titleEl = tds.get(1);
		String link = "https" + LINE_RECRUIT_HOST + titleEl.select("a").attr("href").trim();

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
