package com.mino.devjob.recruit.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = WoowaRecruitDto.WoowaRecruitDtoBuilder.class)
public class WoowaRecruitDto {
	private long jobIdx;
	private String jobTitle; // 내용(제목)
	private String careerName; // 정규직/인턴...
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDate eDate; // 기간
	private String businessName; // 분류

	@JsonPOJOBuilder(withPrefix = "")
	public static class WoowaRecruitDtoBuilder {
	}

	public Recruit toRecruit() {
		return Recruit.builder()
			.index(jobIdx)
			.company(CompanyType.WOOWA.name())
			.title(jobTitle)
			.link("https://www.woowahan.com/#/recruit/tech")
			.jobType(careerName)
			.term(eDate)
			.tags(businessName)
			.companyType(CompanyType.WOOWA.name())
			.build();
	}
}
