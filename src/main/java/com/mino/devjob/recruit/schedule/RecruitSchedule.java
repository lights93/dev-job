package com.mino.devjob.recruit.schedule;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.service.CrawlService;
import com.mino.devjob.recruit.service.RecruitService;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class RecruitSchedule {
	private final Map<CompanyType, CrawlService> crawlServiceMap;
	private final RecruitService recruitService;

	@Scheduled(fixedDelay = 60 * 60 * 1000) // 1 hour
	public void refreshRecruit() {
		// TODO 지저분 ㅠㅠ
		List<Recruit> recruits = recruitService.getRecruits(true).collectList().blockOptional().orElseThrow();

		recruitService.deleteAll().block();

		Flux.fromIterable(crawlServiceMap.values())
			.flatMap(CrawlService::crawl)
			.collectList()
			.flatMapMany(recruitService::saveAll)
			.collectList()
			.block();

		recruits.forEach(recruit -> recruitService.update(recruit).block());
	}
}
