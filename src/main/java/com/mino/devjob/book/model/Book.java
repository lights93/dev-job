package com.mino.devjob.book.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document(collection = "book")
public class Book {
	@Id
	private final long id;
	private final String name;
	private final String intro; // 소개
	private final String link; // 링크
	private final String author; // 저자
	private final LocalDate publishDate; // 출판일
	private final String publisher; // 출판사
	private final int price; // 가격
	private final int favorite; // 관심
}
