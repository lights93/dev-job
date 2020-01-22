package com.mino.devjob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CommonConfig {
	@Autowired
	public void configMapper(final ObjectMapper mapper) {
		// LocalDateTime 파싱을 위해 추가
		mapper.findAndRegisterModules();
		// 대문자로 시작하는 경우 처리하기 위해 추가
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
	}
}
