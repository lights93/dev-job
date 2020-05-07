package com.mino.devjob.recruit.schedule;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.service.CrawlService;
import com.mino.devjob.recruit.service.RecruitService;
import com.mino.devjob.recruit.type.CompanyType;
import reactor.core.publisher.Flux;

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
		//		Recruit recruit2 = Recruit.builder().title("title2").build();

		Flux<Recruit> recruitFlux = Flux.just(recruit);
		//		Flux<Recruit> recruitFlux2 = Flux.just(recruit2);

		Mockito.when(recruitService.getRecruitsByFavorite(Mockito.anyInt()))
			.thenReturn(recruitFlux);
		//
		//		Mockito.when(recruitService.deleteAll(Mockito.anyList()))
		//			.thenReturn(Mono.empty());
		//
		Mockito.when(crawlServiceMap.values())
			.thenReturn(List.of(crawlService));
		//
		//		Mockito.when(crawlService.crawl())
		//			.thenReturn(recruitFlux2);
		//
		//		Mockito.when(recruitService.notExistsByIndexAndCompanyAndFavorite(Mockito.any(Recruit.class)))
		//			.thenReturn(Mono.just(true));
		//
		//		Mockito.when(recruitService.saveAll(Mockito.eq(List.of(recruit2))))
		//			.thenReturn(Flux.empty());

		recruitSchedule.refreshRecruit();

		//		Mockito.verify(recruitService, Mockito.times(1))
		//			.deleteAll(Mockito.eq(List.of(recruit)));

		//		Mockito.verify(recruitService, Mockito.times(1))
		//			.saveAll(Mockito.eq(List.of(recruit2)));
	}
}