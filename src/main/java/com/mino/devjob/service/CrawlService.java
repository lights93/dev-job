package com.mino.devjob.service;

import com.mino.devjob.model.Recruit;
import reactor.core.publisher.Flux;

public interface CrawlService {
	Flux<Recruit> crawl();
}
