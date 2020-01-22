package com.mino.devjob.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.mino.devjob.model.KakaoRecruit;
import reactor.core.publisher.Flux;

@Service("kakao")
public class CrawlKakaoService implements CrawlService<KakaoRecruit> {
	private static final String KAKAO_RECRUIT_URL = "https://careers.kakao.com";
	private static final Pattern NUMBER_PART_PATTERN = Pattern.compile("\\d+");

	public Flux<KakaoRecruit> crawl() throws IOException {
		Document document = Jsoup.connect(KAKAO_RECRUIT_URL + "/jobs")
			.followRedirects(false)
			.method(Connection.Method.GET)
			.data("part", "TECHNOLOGY")
			.data("page", "8")
			.execute()
			.parse();

		final Elements titles = document.select(".txt_tit");
		final Elements companyTypes = document.select(".item_board .field_front");
		final Elements links = document.select(".link_notice");

		final Elements details = document.select(".txt_period");
		final Elements tagElements = document.select(".list_tag.wrap_tag");

		int cnt = titles.size();

		return Flux.range(0, cnt)
			.map(i -> {
				Element detail = details.get(i);
				Elements detailTexts = detail.select("span span:not(.txt_bar)");
				String address = "";
				String jobType = "";
				String term = "";
				if (detailTexts.size() > 0) {
					address = detailTexts.get(0).text().trim();
					jobType = detailTexts.get(1).text().trim();
					term = detailTexts.get(2).text().trim();
				}

				Matcher matcher = NUMBER_PART_PATTERN.matcher(term);
				LocalDate end = Flux.fromStream(matcher.results())
					.map(MatchResult::group)
					.collectList()
					.filter(list -> list.size() > 2)
					.map(list -> LocalDate.of(Integer.parseInt(list.get(0)), Integer.parseInt(list.get(1)),
						Integer.parseInt(list.get(2))))
					.blockOptional()
					.orElse(LocalDate.of(2999, 12, 31));

				Element tagEl = tagElements.get(i);
				Elements tagTexts = tagEl.select("a");

				String tags = String.join(", ", tagTexts.eachText());

				String link = KAKAO_RECRUIT_URL + links.get(i).attr("href");

				return KakaoRecruit.builder()
					.company("kakao")
					.title(titles.get(i).text())
					.companyType(companyTypes.get(i).text())
					.link(link)
					.address(address)
					.jobType(jobType)
					.term(end)
					.tags(tags)
					.build();
			});
	}
}
