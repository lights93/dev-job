package com.mino.devjob.user.service;

import org.springframework.stereotype.Service;

import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.repository.OAuth2UserInfoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
	private final OAuth2UserInfoRepository oAuth2UserInfoRepository;

	public Mono<OAuth2UserInfo> save(OAuth2UserInfo oAuth2UserInfo) {
		return oAuth2UserInfoRepository.save(oAuth2UserInfo);
	}
}
