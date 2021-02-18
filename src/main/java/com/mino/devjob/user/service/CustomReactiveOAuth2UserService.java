package com.mino.devjob.user.service;

import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.repository.OAuth2UserInfoRepository;
import com.mino.devjob.user.type.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomReactiveOAuth2UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final OAuth2UserInfoRepository oAuth2UserInfoRepository;

	@Override
	public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		final DefaultReactiveOAuth2UserService delegate = new DefaultReactiveOAuth2UserService();
		final String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();

		Mono<OAuth2User> oAuth2User = delegate.loadUser(userRequest);

		return oAuth2User.flatMap(e -> {
			OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(clientRegistrationId,
				e.getAttributes());
			return oAuth2UserInfoRepository
				.findByName(oAuth2UserInfo.getName())
				.switchIfEmpty(Mono.defer(() -> oAuth2UserInfoRepository.save(oAuth2UserInfo)));
		});
	}
}
