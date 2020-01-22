package com.mino.devjob.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mino.devjob.model.WoowaRecruit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString(exclude = {"contents"})
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoowaRecruitDto {
	private int jobIdx;
	private String link;
	private String jobTitle; // 내용(제목)
	private String careerName; // 정규직/인턴...
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDate eDate; // 기간
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime sDate; // 기간
	private String businessName; // 분류
	private String contents;

	public WoowaRecruit toWoowaRecruit() {
		return WoowaRecruit.builder()
			.company("woowa")
			.title(jobTitle)
			.link("https://www.woowahan.com/#/recruit/tech")
			.jobType(careerName)
			.term(eDate)
			.tags(businessName)
			.build();
	}
}
