package com.mino.devjob.recruit.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.dto.DaangnRequestDto;
import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service("DAANGN")
public class CrawlDaangnService implements CrawlService {
	private static final String DAANGN_RECRUIT_URL = "https://www.notion.so";
	private static final Pattern BRACKETS_PATTERN = Pattern.compile("\\((.*?)\\)");
	private static final DaangnRequestDto DAANGN_REQUEST_DTO =
		DaangnRequestDto.builder()
			.pageId("07ca1fda-2258-4d60-a48e-f43a8cf9bab0")
			.limit(50)
			.chunkNumber(0)
			.verticalColumns(false)
			.build();

	private final WebClient webClient = WebClient.create(DAANGN_RECRUIT_URL);

	@Override
	public Flux<Recruit> crawl() {
		return getDaangnDocument()
			.map(this::buildDaangnRecruit);
	}

	private Flux<Map<String, Object>> getDaangnDocument() {
		return webClient
			.post()
			.uri(uriBuilder -> uriBuilder
				.pathSegment("api", "v3", "loadPageChunk")
				.build())
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(DAANGN_REQUEST_DTO))
			.retrieve()
			.bodyToMono(Map.class)
			.map(dto -> ((Map<String, Map<String, Map<String, Map<String, Object>>>>)dto.get("recordMap")).get("block"))
			.map(result -> new ArrayList<>(result.values()))
			.flatMapMany(Flux::fromIterable)
			.map(item -> item.get("value"))
			.filter(v -> "toggle".equals(v.get("type")))
			.filter(v -> "07ca1fda-2258-4d60-a48e-f43a8cf9bab0".equals(v.get("parent_id")));

	}

	private Recruit buildDaangnRecruit(Map<String, Object> valueMap) {
		String titleText = (String)((Map<String, List<List<Object>>>)valueMap.get("properties"))
			.get("title").get(0).get(0);

		Matcher matcher = BRACKETS_PATTERN.matcher(titleText);

		List<String> matchList = new ArrayList<>();

		while (matcher.find()) {//Finds Matching Pattern in String
			matchList.add(matcher.group(1).trim());//Fetching Group from String
		}

		String tags = "";
		String jobType = "";

		if (matchList.size() > 1) { // 태그가 있는 경우
			tags = matchList.get(0);
			jobType = matchList.get(1);
		} else if (matchList.size() == 1) {
			jobType = matchList.get(0);
		}

		String title = titleText.trim();

		if (titleText.contains("(")) {
			title = titleText.substring(0, titleText.indexOf("(")).trim();
		}

		LocalDate end = LocalDate.of(2999, 12, 31);

		return Recruit.builder()
			.index((long)valueMap.get("created_time"))
			.company(CompanyType.DAANGN.name())
			.title(title)
			.link(DAANGN_RECRUIT_URL)
			.jobType(jobType)
			.term(end)
			.companyType(CompanyType.DAANGN.name())
			.tags(tags)
			.build();
	}
}
