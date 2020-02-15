package com.mino.devjob.recruit.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service("KAKAO")
@RequiredArgsConstructor
@Slf4j
public class CrawlKakaoService implements CrawlService {
	private static final String KAKAO_RECRUIT_URL = "https://careers.kakao.com";
	private static final Pattern NUMBER_PART_PATTERN = Pattern.compile("\\d+");

	@Override
	public Flux<Recruit> crawl() {
		return Mono.fromCallable(() -> this.getKakaoDocument(1))
			.subscribeOn(Schedulers.elastic())
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(document -> document.select(".link_job.link_job1").text().trim())
			.map(NUMBER_PART_PATTERN::matcher)
			.filter(Matcher::find)
			.map(matcher -> (Integer.parseInt(matcher.group(0)) + 10 - 1) / 10)
			.flatMapMany(pages -> Flux.range(1, pages))
			.flatMap(page -> Mono.fromCallable(() -> this.getKakaoDocument(page))
				.subscribeOn(Schedulers.elastic()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.flatMap(this::buildKakaoRecruit);
	}

	private Optional<Document> getKakaoDocument(int page) {
		try {
			return Optional.ofNullable(Jsoup.connect(KAKAO_RECRUIT_URL + "/jobs")
				.followRedirects(false)
				.method(Connection.Method.GET)
				.data("part", "TECHNOLOGY")
				.data("page", Integer.toString(page))
				.execute()
				.parse());
		} catch (IOException e) {
			log.error("kakao parse error, page: {}", page, e);
			return Optional.empty();
		}
	}

	private Flux<Recruit> buildKakaoRecruit(Document doc) {
		final Elements titles = doc.select(".txt_tit");
		final Elements companyTypes = doc.select(".item_board .field_front");
		final Elements links = doc.select(".link_notice");

		final Elements details = doc.select(".txt_period");
		final Elements tagElements = doc.select(".list_tag.wrap_tag");

		int cnt = titles.size();

		return Flux.range(0, cnt)
			.map(i -> {
				Element detail = details.get(i);
				Elements detailTexts = detail.select("span span:not(.txt_bar)");
				String jobType = "";
				String term = "";
				if (detailTexts.size() > 0) {
					jobType = detailTexts.get(1).text().trim();
					term = detailTexts.get(2).text().trim();
				}

				List<String> matchList = NUMBER_PART_PATTERN.matcher(term).results()
					.map(MatchResult::group)
					.collect(Collectors.toList());

				LocalDate end = LocalDate.of(2999, 12, 31);

				if (matchList.size() > 2) {
					end = LocalDate.of(Integer.parseInt(matchList.get(0)), Integer.parseInt(matchList.get(1)),
						Integer.parseInt(matchList.get(2)));
				}

				Element tagEl = tagElements.get(i);
				Elements tagTexts = tagEl.select("a");

				String tags = String.join(", ", tagTexts.eachText());

				String link = KAKAO_RECRUIT_URL + links.get(i).attr("href").trim();

				int startIdx = link.indexOf("/jobs/P-") + 8;
				int endIdx = link.indexOf("?part");

				int index = Integer.parseInt(link.substring(startIdx, endIdx));

				return Recruit.builder()
					.index(index)
					.company(CompanyType.KAKAO.name())
					.title(titles.get(i).text())
					.companyType(companyTypes.get(i).text())
					.link(link)
					.jobType(jobType)
					.term(end)
					.tags(tags)
					.build();
			});
	}
}
