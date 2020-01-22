package com.mino.devjob.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mino.devjob.model.NaverRecruit;
import com.mino.devjob.type.NaverEntType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString(exclude = {"jobText"})
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverRecruitDto {
	public static final String NAVER_RECRUIT_URL = "https://recruit.navercorp.com/naver/job/detail/developer";
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

	private String company; // 회사
	private long annoId; // id
	private String staYmd; // 시작일자
	private String endYmd; // 종료일자
	private String entTypeCd; // 001 신입 002 경력 003 모두 004 인턴
	private String jobNm; // 제목
	private String sysCompanyCd; // 계열사
	private String stateCd; // 004 006
	private String reqTypeCd; // 1200 ??
	private String jobKeyword; // # 백엔드 개발
	private String addSysCompanyCd; // 추가 계열사 정보
	private String openYn; //Y

	public NaverRecruit toNaverRecruit() {
		return NaverRecruit.builder()
			.company("naver")
			.title(jobNm)
			.link(NAVER_RECRUIT_URL + "?annoId=" + annoId)
			.jobType(NaverEntType.getByCode(entTypeCd))
			.term(LocalDate.parse(endYmd, DATE_FORMAT))
			.companyType(sysCompanyCd)
			.tags(jobKeyword)
			.build();
	}
}
