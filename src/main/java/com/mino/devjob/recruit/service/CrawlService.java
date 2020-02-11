package com.mino.devjob.recruit.service;

import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;

public interface CrawlService {
	Flux<Recruit> crawl();
}
