package com.mino.devjob.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthProvider {
	GOOGLE("GOOGLE"),
	KAKAO("KAKAO"),
	NAVER("NAVER");

	private final String providerType;
}