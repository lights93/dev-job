package com.mino.devjob.recruit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.mino.devjob.recruit.dto.TossRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service("TOSS")
public class CrawlTossService implements CrawlService {
	private static final String TOSS_CAREER_API_URL = "https://toss.im/careers/api/greenhouse/jobs";

	private final WebClient webClient;

	@SneakyThrows
	@Autowired
	public CrawlTossService(WebClient webClient, ObjectMapper mapper) {
		ObjectMapper newMapper = mapper.copy();
		newMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

		this.webClient = webClient.mutate()
			.exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer -> {
						configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(newMapper));
						configurer.defaultCodecs().maxInMemorySize(Integer.MAX_VALUE);
				}).build())
			.build();
	}

	@Override
	public Flux<Recruit> crawl() {
		return webClient
			.get()
			.uri(TOSS_CAREER_API_URL)
			.retrieve()
			.bodyToMono(TossRecruitDto.class)
			.flatMapIterable(TossRecruitDto::getJobs)
			.filter(TossRecruitDto.Job::isEngineering)
			.map(TossRecruitDto.Job::toRecruit);
	}
}
