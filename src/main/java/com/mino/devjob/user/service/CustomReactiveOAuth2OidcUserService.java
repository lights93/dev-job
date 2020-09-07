package com.mino.devjob.user.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.type.OAuth2UserInfoFactory;
import com.mino.devjob.user.repository.OAuth2UserInfoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomReactiveOAuth2OidcUserService implements ReactiveOAuth2UserService<OidcUserRequest, OidcUser> {
	private final OAuth2UserInfoRepository oAuth2UserInfoRepository;

	@Override
	public Mono<OidcUser> loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		final OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();
		final String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();

		Mono<OidcUser> oAuth2User = delegate.loadUser(userRequest);

		return oAuth2User.flatMap(e -> {
			OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(clientRegistrationId, e.getAttributes());
			return oAuth2UserInfoRepository
				.findByName(oAuth2UserInfo.getName())
				.switchIfEmpty(Mono.defer(() -> oAuth2UserInfoRepository.save(oAuth2UserInfo)));
		});
	}
}