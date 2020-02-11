package com.mino.devjob.book.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Yes24CategoryType {
	OS_DB("001001003025", "OS/데이터베이스"),
	COMPUTER_ENGINEERING("001001003031", "컴퓨터공학"),
	PROGRAMMING_LANGUAGE("001001003022", "프로그래밍언어"),
	NETWORK_SECURITY("001001003024", "네트워크/해킹/보안");

	private final String code;
	private final String category;
}
