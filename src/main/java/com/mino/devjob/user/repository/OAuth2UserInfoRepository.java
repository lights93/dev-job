package com.mino.devjob.user.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.mino.devjob.user.model.OAuth2UserInfo;
import reactor.core.publisher.Mono;

@Repository
public interface OAuth2UserInfoRepository extends ReactiveMongoRepository<OAuth2UserInfo, String> {
	Mono<OAuth2UserInfo> findByName(String name);
}
