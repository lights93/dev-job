package com.mino.devjob.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mino.devjob.user.type.CurrentUser;
import com.mino.devjob.user.model.OAuth2UserInfo;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

	@GetMapping("api/user")
	@ApiOperation("user 가져오기 테스트")
	Mono<OAuth2UserInfo> getUserInfo(@CurrentUser OAuth2UserInfo oAuth2UserInfo) {
		return Mono.just(oAuth2UserInfo);
	}
}
