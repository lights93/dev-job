package com.mino.devjob.recruit.schedule;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mino.devjob.recruit.service.CrawlService;
import com.mino.devjob.recruit.service.RecruitService;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class RecruitSchedule {
	private final Map<CompanyType, CrawlService> crawlServiceMap;
	private final RecruitService recruitService;
	private static final Scheduler SCHEDULER = Schedulers.boundedElastic();

	@Scheduled(fixedDelay = 60 * 60 * 1000) // 1 hour
	public void refreshRecruit() {
		recruitService.getRecruitsByFavorite(0)
			.collectList()
			.flatMap(recruitService::deleteAll)
			.then(Mono.just(crawlServiceMap.values()))
			.flatMapMany(Flux::fromIterable)
			.flatMap(CrawlService::crawl)
			.filterWhen(r -> recruitService.notExistsByIndexAndCompanyAndFavoriteIsNot(r, 0))
			.collectList()
			.flatMapMany(recruitService::saveAll)
			.collectList()
			.subscribeOn(SCHEDULER)
			.subscribe();
	}
}
