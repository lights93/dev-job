package com.mino.devjob.recruit.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.mino.devjob.recruit.model.Recruit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RecruitRepository extends ReactiveMongoRepository<Recruit, ObjectId> {
	Mono<Boolean> existsByIndexAndCompany(Long index, String company);

	Flux<Recruit> findAllByCompany(String company);

	Mono<Recruit> findByIndexAndCompany(Long index, String company);

	Flux<Recruit> findAllByFavorite(boolean favorite);
}
