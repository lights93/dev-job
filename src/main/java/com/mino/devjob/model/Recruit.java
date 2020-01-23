package com.mino.devjob.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Recruit {
	private final long index;
	private final String company;
	private final String title;
	private final String link; // 링크
	private final String jobType; // 정규직/인턴...
	private final LocalDate term; // 영입종료조건
	private final String companyType; // 계열사
	private final String tags; // 태그
}
