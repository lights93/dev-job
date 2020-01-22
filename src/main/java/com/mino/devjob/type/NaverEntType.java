package com.mino.devjob.type;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NaverEntType {
	NEW("001", "신입"),
	CAREER("002", "경력"),
	ALL("003", "무관"),
	INTERN("004", "인턴");

	private final String code;
	private final String name;

	public static NaverEntType getByCode(String code) {
		return Arrays.stream(values())
			.filter(a -> a.getCode().equals(code))
			.findAny()
			.orElseThrow(IllegalAccessError::new);
	}

	@JsonValue
	public String getName() {
		return name;
	}
}
