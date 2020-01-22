package com.mino.devjob.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mino.devjob.service.CrawlService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CrawlController {
	private final Map<String, CrawlService> crawlServiceMap;

	@GetMapping("crawl/{company}")
	@ApiOperation(value = "크롤링 서비스")
	public Flux<?> crawl(@PathVariable String company) throws IOException {
		if (crawlServiceMap.containsKey(company) == false) {
			throw new IllegalArgumentException("잘못된 사명입니다.");
		}

		return crawlServiceMap.get(company).crawl();
	}

}
