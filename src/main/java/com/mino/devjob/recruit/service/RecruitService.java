package com.mino.devjob.recruit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.repository.RecruitRepository;
import com.mino.devjob.recruit.type.CompanyType;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RecruitService {
	private final RecruitRepository recruitRepository;

	public Flux<Recruit> getRecruits(String company) {
		CompanyType companyType = CompanyType.valueOf(company);

		if (companyType == CompanyType.ALL) {
			return recruitRepository.findAll();
		}

		return recruitRepository.findAllByCompany(company);
	}

	public Mono<Long> getAllCount() {
		return recruitRepository.count();
	}

	public Mono<Recruit> update(Recruit recruit) {
		return recruitRepository.findByIndexAndCompany(recruit.getIndex(), recruit.getCompany())
			.map(r -> Recruit.builder()
				.index(r.getIndex())
				.company(r.getCompany())
				.companyType(r.getCompanyType())
				.title(r.getTitle())
				.jobType(r.getJobType())
				.link(r.getLink())
				.tags(r.getTags())
				.favorite(recruit.isFavorite())
				.term(r.getTerm())
				.objectId(r.getObjectId())
				.build()
			)
			.flatMap(recruitRepository::save);
	}

	public Flux<Recruit> getRecruits(boolean favorite) {
		return recruitRepository.findAllByFavorite(favorite);
	}

	public Mono<Void> deleteAll() {
		return recruitRepository.deleteAll();
	}

	public Flux<Recruit> saveAll(List<Recruit> recruits) {
		return recruitRepository.saveAll(recruits);
	}
}
