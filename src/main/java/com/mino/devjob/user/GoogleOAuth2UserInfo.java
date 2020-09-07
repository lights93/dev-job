package com.mino.devjob.user;

import java.util.Map;

import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.type.AuthProvider;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	protected AuthProvider getAuthProviderEnum() {
		return AuthProvider.GOOGLE;
	}

	@Override
	protected void setAttribute() {
		this.name = String.valueOf(attributes.get("name"));
	}
}
