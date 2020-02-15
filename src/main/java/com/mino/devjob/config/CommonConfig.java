package com.mino.devjob.config;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mino.devjob.recruit.service.CrawlService;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CommonConfig {
	private final Map<String, CrawlService> crawlServiceMap;

	@Bean
	public Map<CompanyType, CrawlService> createCrawlServiceEnumMap() {
		return Arrays.stream(CompanyType.values())
			.filter(companyType -> companyType != CompanyType.ALL)
			.collect(Collectors.toMap(
				Function.identity(),
				key -> crawlServiceMap.get(key.toString()),
				(l, r) -> {
					throw new IllegalArgumentException();
				},
				() -> new EnumMap<>(CompanyType.class)));
	}
}
