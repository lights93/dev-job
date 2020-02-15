package com.mino.devjob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class MapperConfig {
	@Bean
	public ObjectMapper mapper(Jackson2ObjectMapperBuilder mapperBuilder) {
		return mapperBuilder
			.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES) // 대문자로 시작하는 경우 처리
			.modules(new JavaTimeModule()) // LocalDateTime 파싱을 위해 추가
			.build();
	}
}
