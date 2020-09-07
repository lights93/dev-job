package com.mino.devjob.user.type;

import java.util.Map;

import com.mino.devjob.user.GoogleOAuth2UserInfo;
import com.mino.devjob.user.KakaoOAuth2UserInfo;
import com.mino.devjob.user.NaverOAuth2UserInfo;
import com.mino.devjob.user.model.OAuth2UserInfo;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.getProviderType())) {
			return new GoogleOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(AuthProvider.KAKAO.getProviderType())) {
			return new KakaoOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(AuthProvider.NAVER.getProviderType())) {
			return new NaverOAuth2UserInfo(attributes);
		} else {
			throw new IllegalArgumentException("unsupported provider");
		}
	}
}
