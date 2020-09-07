package com.mino.devjob.recruit.service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
@Service("KAKAO")
@Slf4j
public class CrawlKakaoService implements CrawlService {
	private static final String KAKAO_RECRUIT_HOST = "careers.kakao.com";
	private static final Pattern NUMBER_PART_PATTERN = Pattern.compile("\\d+");

	private final WebClient webClient;

	@Override
	public Flux<Recruit> crawl() {
		return getPageCount()
			.flatMapMany(this::crawlKakao);
	}

	private Mono<Integer> getPageCount() {
		return getKakaoDocument(1)
			.onErrorContinue((error, element) -> log.error("get kakao error!! page: 1", error))
			.map(document -> document.select(".link_job.link_job1 .emph_num").text().trim())
			.map(pages -> (Integer.parseInt(pages) + 15 - 1) / 15);
	}

	private Flux<Recruit> crawlKakao(int pageCount) {
		return Flux.range(1, pageCount)
			.flatMap(this::getKakaoDocument)
			.onErrorContinue((error, element) -> log.error("get kakao error!!", error))
			.flatMap(this::buildKakaoRecruit);
	}

	private Mono<Document> getKakaoDocument(int page) {
		return webClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.scheme("https")
				.host(KAKAO_RECRUIT_HOST)
				.path("jobs")
				.queryParam("part", "TECHNOLOGY")
				.queryParam("page", Integer.toString(page))
				.queryParam("company", "ALL")
				.build())
			.retrieve()
			.bodyToMono(String.class)
			.map(Jsoup::parse);
	}

	private Flux<Recruit> buildKakaoRecruit(Document doc) {
		final Elements titles = doc.select(".tit_jobs");
		final Elements links = doc.select(".link_jobs");

		final Elements termInfos = doc.select(".list_info");
		final Elements details = doc.select(".area_info");

		int cnt = titles.size();

		return Flux.range(0, cnt)
			.map(i -> {
				Element termInfo = termInfos.get(i);
				String term = termInfo.selectFirst(".screen_out").text().trim();

				List<String> matchList = NUMBER_PART_PATTERN.matcher(term).results()
					.map(MatchResult::group)
					.collect(Collectors.toList());

				LocalDate end = LocalDate.of(2999, 12, 31);

				if (matchList.size() > 2) {
					end = LocalDate.of(Integer.parseInt(matchList.get(0)), Integer.parseInt(matchList.get(1)),
						Integer.parseInt(matchList.get(2)));
				}

				Element detail = details.get(i);
				String companyType = detail.select(".item_subinfo dd").get(0).text().trim();
				String jobType = detail.select(".item_subinfo dd").get(1).text().trim();

				Elements tagTexts = detail.select(".list_tag.wrap_tag a");

				String tags = String.join(", ", tagTexts.eachText());

				String link = "https://" + KAKAO_RECRUIT_HOST + links.get(i).attr("href").trim();

				int startIdx = link.indexOf("-") + 1;
				int endIdx = link.indexOf("?");

				int index = Integer.parseInt(link.substring(startIdx, endIdx));

				return Recruit.builder()
					.index(index)
					.company(CompanyType.KAKAO.name())
					.title(titles.get(i).text())
					.companyType(companyType)
					.link(link)
					.jobType(jobType)
					.term(end)
					.tags(tags)
					.build();
			});
	}
}
