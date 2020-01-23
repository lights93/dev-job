package com.mino.devjob.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mino.devjob.model.Recruit;
import com.mino.devjob.type.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoowaRecruitDto {
	private long jobIdx;
	private String jobTitle; // 내용(제목)
	private String careerName; // 정규직/인턴...
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDate eDate; // 기간
	private String businessName; // 분류

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
