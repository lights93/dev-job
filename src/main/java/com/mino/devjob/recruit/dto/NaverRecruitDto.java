package com.mino.devjob.recruit.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.type.CompanyType;
import com.mino.devjob.recruit.type.NaverEntType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = NaverRecruitDto.NaverRecruitDtoBuilder.class)
public class NaverRecruitDto {
	public static final String NAVER_RECRUIT_URL = "https://recruit.navercorp.com/naver/job/detail/developer";
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

	private long annoId; // id
	private String endYmd; // 종료일자
	private String entTypeCd; // 001 신입 002 경력 003 모두 004 인턴
	private String jobNm; // 제목
	private String sysCompanyCd; // 계열사
	private String jobKeyword; // # 백엔드 개발

	@JsonPOJOBuilder(withPrefix = "")
	public static class NaverRecruitDtoBuilder {
	}

	public Recruit toRecruit() {
		return Recruit.builder()
			.index(annoId)
			.company(CompanyType.NAVER.name())
			.title(jobNm)
			.link(NAVER_RECRUIT_URL + "?annoId=" + annoId)
			.jobType(NaverEntType.getByCode(entTypeCd).getType())
			.term(LocalDate.parse(endYmd, DATE_FORMAT))
			.companyType(sysCompanyCd)
			.tags(jobKeyword)
			.build();
	}
}
