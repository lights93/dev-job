package com.mino.devjob.book.service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.book.type.Yes24CategoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlYes24Service {
	private static final String YES24_HOST = "www.yes24.com";
	private static final Pattern NUMBER_PART_PATTERN = Pattern.compile("\\d+");

	private final WebClient webClient;

	public Flux<Book> crawl() {
		return Flux.fromArray(Yes24CategoryType.values())
			.map(Yes24CategoryType::getCode)
			.flatMap(this::getYes24Document)
			.onErrorContinue((error, element) -> log.error("get yes24 error!!", error))
			.flatMap(this::buildYes24Book);
	}

	private Mono<Document> getYes24Document(String code) {
		return webClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.scheme("http")
				.host(YES24_HOST)
				.pathSegment("24", "Category", "Display", code)
				.queryParam("ParamSortTp", "04") // 신상품 순 정렬
				.queryParam("FetchSize", "50")
				.queryParam("pageNumber", "1")
				.build())
			.retrieve()
			.bodyToMono(String.class)
			.map(Jsoup::parse);
	}

	private Flux<Book> buildYes24Book(Document document) {
		final Elements goodsNames = document.select("#category_layout .goods_name a:not(.bgYUI)");

		final Elements intros = document.select("#category_layout .goods_name .gd_nameE");

		final Elements authors = document.select("#category_layout .goods_pubGrp .goods_auth");
		final Elements publishers = document.select("#category_layout .goods_pubGrp .goods_pub");
		final Elements dates = document.select("#category_layout .goods_pubGrp .goods_date");
		final Elements prices = document.select("#category_layout .goods_price .yes_b");

		final int cnt = goodsNames.size();

		return Flux.range(0, cnt)
			.map(i -> {
				String name = goodsNames.get(i).text().trim();

				String link = "http://" + YES24_HOST + goodsNames.get(i).attr("href").trim();

				int startIdx = link.lastIndexOf("/") + 1;
				long id = Long.parseLong(link.substring(startIdx));

				String intro = intros.get(i).text().trim();
				String author = authors.get(i).text().trim();
				String publisher = publishers.get(i).text().trim();
				int price = Integer.parseInt(prices.get(i).text().trim().replaceAll(",", ""));

				String date = dates.get(i).text().trim();

				List<String> matchList = NUMBER_PART_PATTERN.matcher(date).results()
					.map(MatchResult::group)
					.collect(Collectors.toList());

				LocalDate publishDate = LocalDate.of(Integer.parseInt(matchList.get(0)),
					Integer.parseInt(matchList.get(1)), 1);

				return Book.builder()
					.id(id)
					.name(name)
					.intro(intro)
					.link(link)
					.author(author)
					.publishDate(publishDate)
					.publisher(publisher)
					.price(price)
					.build();
			});
	}
}

