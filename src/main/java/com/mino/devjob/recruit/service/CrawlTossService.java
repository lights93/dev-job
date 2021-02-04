package com.mino.devjob.recruit.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.model.Recruit;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service("TOSS")
public class CrawlTossService implements CrawlService {
	private static final String TOSS_HOST = "toss.im";

	private final WebClient webClient;

	@Override
	public Flux<Recruit> crawl() {
		return null;
	}

	private Mono<Document> getTossDocument() {
		return null;
	}

}
