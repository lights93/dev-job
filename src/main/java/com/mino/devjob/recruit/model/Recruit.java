package com.mino.devjob.recruit.model;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document(collection = "recruit")
public class Recruit {
	@Id
	private ObjectId objectId;
	@Indexed
	private final long index;
	@Indexed
	private final String company;
	private final String title;
	private final String link; // 링크
	private final String jobType; // 정규직/인턴...
	private final LocalDate term; // 영입종료조건
	private final String companyType; // 계열사
	private final String tags; // 태그
	private final boolean favorite; // 관심
}
