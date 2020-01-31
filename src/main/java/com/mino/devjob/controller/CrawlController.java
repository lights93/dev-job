package com.mino.devjob.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mino.devjob.model.Recruit;
import com.mino.devjob.service.CrawlService;
import com.mino.devjob.type.CompanyType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CrawlController {
	private final Map<CompanyType, CrawlService> crawlServiceMap;

	// TODO 배치 or 스케줄링
	@GetMapping("crawl/{company}")
	@ApiOperation(value = "크롤링 서비스")
	public Flux<Recruit> crawl(@PathVariable String company) {
		CompanyType companyType = CompanyType.valueOf(company.toUpperCase());

		if (companyType == CompanyType.ALL) {
			return Flux.fromIterable(crawlServiceMap.values())
				.flatMap(CrawlService::crawl);
		}

		return crawlServiceMap.get(companyType).crawl();
	}

	@GetMapping("/companies")
	@ApiOperation(value = "company 목록 조회")
	public Flux<CompanyType> getCompanies() {
		return Flux.fromArray(CompanyType.values());
	}

}
