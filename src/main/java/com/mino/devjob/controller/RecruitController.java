package com.mino.devjob.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mino.devjob.model.Recruit;
import com.mino.devjob.service.RecruitService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/recruits")
@RequiredArgsConstructor
public class RecruitController {
	private final RecruitService recruitService;

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
	public Mono<Recruit> putRecruit(@RequestBody Recruit recruit) {
		return recruitService.update(recruit);
	}

}
