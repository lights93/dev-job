package com.mino.devjob.user;

import java.util.Map;

import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.type.AuthProvider;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

	public NaverOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	protected AuthProvider getAuthProviderEnum() {
		return AuthProvider.NAVER;
	}

	@Override
	protected void setAttribute() {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");

		this.name = String.valueOf(response.get("id"));
	}
}