package com.mino.devjob.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mino.devjob.book.model.Book;
import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.user.model.OAuth2UserInfo;
import com.mino.devjob.user.service.UserService;
import com.mino.devjob.user.type.CurrentUser;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping
	@ApiOperation("user 가져오기")
	Mono<OAuth2UserInfo> getUserInfo(@CurrentUser OAuth2UserInfo oAuth2UserInfo) {
		return Mono.just(oAuth2UserInfo);
	}

	@PutMapping("recruit")
	@ApiOperation(value = "유저의 recruit 정보 수정")
	public Mono<OAuth2UserInfo> putRecruit(@CurrentUser OAuth2UserInfo oAuth2UserInfo, @RequestBody Recruit recruit) {
		oAuth2UserInfo.addRecruit(recruit);
		return userService.save(oAuth2UserInfo);
	}

	@PutMapping("book")
	@ApiOperation(value = "유저의 책 정보 DB 수정")
	public Mono<OAuth2UserInfo> putBook(@CurrentUser OAuth2UserInfo oAuth2UserInfo, @RequestBody Book book) {
		oAuth2UserInfo.addBook(book);
		return userService.save(oAuth2UserInfo);
	}
}
