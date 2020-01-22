package com.mino.devjob.model;

import java.time.LocalDate;

import com.mino.devjob.type.NaverEntType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class NaverRecruit {
	private final String company; // 회사
	private final String title; // 제목
	private final String link; // 링크
	private final NaverEntType jobType; // 정규직/인턴...
	private final LocalDate term; // 기간
	private final String companyType; // 계열사
	private final String tags; // # 백엔드 개발
}
