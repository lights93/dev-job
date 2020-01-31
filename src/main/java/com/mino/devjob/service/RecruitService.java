package com.mino.devjob.service;

import org.springframework.stereotype.Service;

import com.mino.devjob.model.Recruit;
import com.mino.devjob.repository.RecruitRepository;
import com.mino.devjob.type.CompanyType;
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
}
