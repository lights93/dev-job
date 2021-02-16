package com.mino.devjob.recruit.controller;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.service.RecruitService;
import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.type.CurrentUser;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/recruits")
@RequiredArgsConstructor
public class RecruitController {
	private final RecruitService recruitService;
	private final ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

	@GetMapping("{company}")
	@ApiOperation(value = "DB 조회")
	public Flux<Recruit> getRecruits(@PathVariable String company) {
		return recruitService.getRecruits(company);
	}

	@GetMapping("count")
	@ApiOperation(value = "DB 카운트")
	public Mono<Long> getAllCount() {
		return recruitService.getAllCount();
	}

	@PutMapping
	@ApiOperation(value = "DB 수정")
	public Mono<Recruit> putRecruit(@CurrentUser OAuth2UserInfo oAuth2UserInfo, @RequestBody Recruit recruit) {
//		if(recruit.getFavorite() == 2) {
//			oAuth2UserInfo.getInteresting().add(recruit.getObjectId());
//		}
//
//		oAuth2UserService.
		return recruitService.update(recruit);
	}

}
