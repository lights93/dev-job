package com.mino.devjob.schedule;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.model.Recruit;
import com.mino.devjob.service.CrawlService;
import com.mino.devjob.service.RecruitService;
import com.mino.devjob.type.CompanyType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class RecruitScheduleTest {
	@InjectMocks
	private RecruitSchedule recruitSchedule;

	@Mock
	private Map<CompanyType, CrawlService> crawlServiceMap;

	@Mock
	private CrawlService crawlService;

	@Mock
	private RecruitService recruitService;

	@Test
	void refreshRecruit() {
		Recruit recruit = Recruit.builder().title("title").build();
		Recruit recruit2 = Recruit.builder().title("title2").build();

		Flux<Recruit> recruitFlux = Flux.just(recruit);
		Flux<Recruit> recruitFlux2 = Flux.just(recruit2);

		Mockito.when(recruitService.getRecruits(Mockito.anyBoolean()))
			.thenReturn(recruitFlux);

		Mockito.when(recruitService.deleteAll())
			.thenReturn(Mono.empty());

		Mockito.when(crawlServiceMap.values())
			.thenReturn(List.of(crawlService));

		Mockito.when(crawlService.crawl())
			.thenReturn(recruitFlux2);

		Mockito.when(recruitService.saveAll(Mockito.eq(List.of(recruit2))))
			.thenReturn(Flux.empty());

		Mockito.when(recruitService.update(Mockito.eq(recruit)))
			.thenReturn(Mono.just(recruit));

		recruitSchedule.refreshRecruit();

		Mockito.verify(recruitService, Mockito.times(1))
			.deleteAll();

		Mockito.verify(recruitService, Mockito.times(1))
			.update(Mockito.eq(recruit));
	}
}