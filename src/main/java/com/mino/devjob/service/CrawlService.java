package com.mino.devjob.service;

import java.io.IOException;

import reactor.core.publisher.Flux;

public interface CrawlService<T> {
	Flux<T> crawl() throws IOException;
}
