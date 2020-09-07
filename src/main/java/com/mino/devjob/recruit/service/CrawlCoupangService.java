package com.mino.devjob.recruit.service;

import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
@Slf4j
@Service("COUPANG")
public class CrawlCoupangService implements CrawlService {
	private static final String COUPANG_RECRUIT_HOST = "rocketyourcareer.kr.coupang.com";

	private final WebClient webClient;

	@Override
	public Flux<Recruit> crawl() {
		return getPageCount()
			.flatMapMany(this::crawlCoupang);
	}

	private Mono<Integer> getPageCount() {
		return getCoupangDocument(1)
			.onErrorContinue((error, element) -> log.error("get coupang error!! page: 1", error))
			.map(document -> document.select(".result-count-number").text().trim())
			.map(Integer::parseInt)
			.map(count -> ((count + 15 - 1) / 15));
	}

	private Mono<Document> getCoupangDocument(int page) {
		return webClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.scheme("https")
				.host(COUPANG_RECRUIT_HOST)
				.path("%EA%B2%80%EC%83%89-%EC%A7%81%EB%AC%B4/%EB%8C%80%ED%95%9C%EB%AF%BC%EA%B5%AD")
				.queryParam("orgIds", "24450")
				.queryParam("alp", "1835841")
				.queryParam("alt", "2")
				.queryParam("ac", "62822")
				.queryParam("p", Integer.toString(page))
				.build())
			.retrieve()
			.bodyToMono(String.class)
			.map(Jsoup::parse);
	}

	private Flux<Recruit> crawlCoupang(int pageCount) {
		return Flux.range(1, pageCount)
			.flatMap(this::getCoupangDocument)
			.onErrorContinue((error, element) -> log.error("get coupang error!!", error))
			.flatMap(this::buildCoupangRecruit);
	}

	private Flux<Recruit> buildCoupangRecruit(Document doc) {
		final Elements titles = doc.select(".searched-job-title");
		final Elements links = doc.select(".searched-job-item a");

		LocalDate end = LocalDate.of(2999, 12, 31);

		int cnt = titles.size();

		return Flux.range(0, cnt)
			.map(i -> {
				String link = "https://" + COUPANG_RECRUIT_HOST + links.get(i).attr("href").trim();
				String idStr = link.substring(link.lastIndexOf('/') + 1);
				int id = Integer.parseInt(idStr);

				return Recruit.builder()
					.index(id)
					.company(CompanyType.COUPANG.name())
					.title(titles.get(i).text().trim())
					.companyType(CompanyType.COUPANG.name())
					.link(link)
					.jobType("")
					.term(end)
					.tags("")
					.build();
			});
	}
}
