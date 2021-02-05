package com.mino.devjob.recruit.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TossRecruitDto {
	private List<Job> jobs;

	@Value
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Job {
		private String absoluteUrl;
		private long id;
		private String title;
		private List<Metadata> metadata;

		public boolean isEngineering() {
			// 4168924003 -> Job Category

			return this.metadata.stream()
				.filter(data -> data.getId() == 4168924003L)
				.map(Metadata::getValue)
				.anyMatch(val -> val.toLowerCase().contains("engineering"));
		}

		public Recruit toRecruit() {

			String jobType = null;
			String companyType = null;
			String tags = null;

			List<Metadata> metaData = this.metadata;
			for (Metadata data : metaData) {
				if (data.getId() == 4112432003L) {
					jobType = data.getValue();
				} else if (data.getId() == 4169410003L) {
					companyType = data.getValue();
				} else if (data.getId() == 4194643003L) {
					tags = data.getValue();
				}
			}

			// 4112432003 -> 고용 형태
			// 4169410003 -> 자회사
			// 4194643003 -> 키워드

			return Recruit.builder()
				.index(id)
				.company(CompanyType.TOSS.name())
				.title(title)
				.link(absoluteUrl)
				.jobType(jobType)
				.term(LocalDate.of(2999, 12, 31))
				.companyType(companyType)
				.tags(tags)
				.build();
		}
	}

	@Value
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Metadata {
		private long id;
		private String name;
		private String value;
	}


}
