package com.mino.devjob.user;

import java.util.Map;

import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.type.AuthProvider;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	protected AuthProvider getAuthProviderEnum() {
		return AuthProvider.KAKAO;
	}

	@Override
	protected void setAttribute() {
		this.name = String.valueOf(attributes.get("id"));
	}
}
