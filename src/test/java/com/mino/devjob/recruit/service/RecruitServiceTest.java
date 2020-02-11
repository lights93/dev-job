package com.mino.devjob.recruit.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.recruit.model.Recruit;
import com.mino.devjob.recruit.repository.RecruitRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest {
	@InjectMocks
	private RecruitService recruitService;

	@Mock
	private RecruitRepository recruitRepository;

	@Test
	void getRecruitsCompany() {
		Recruit recruit = Recruit.builder().title("title").build();
		Recruit recruit2 = Recruit.builder().title("title2").build();

		Flux<Recruit> recruitFlux = Flux.just(recruit);
		Flux<Recruit> recruitFlux2 = Flux.just(recruit2);

		Mockito.when(recruitRepository.findAll())
			.thenReturn(recruitFlux);

		Mockito.when(recruitRepository.findAllByCompany(Mockito.anyString()))
			.thenReturn(recruitFlux2);

		StepVerifier.create(recruitService.getRecruits("ALL"))
			.expectNext(recruit)
			.verifyComplete();

		StepVerifier.create(recruitService.getRecruits("NAVER"))
			.expectNext(recruit2)
			.verifyComplete();

		assertThrows(IllegalArgumentException.class, () -> recruitService.getRecruits("FAIL"));
	}

	@Test
	void getAllCount() {
		Mockito.when(recruitRepository.count()).thenReturn(Mono.just(10L));

		StepVerifier.create(recruitService.getAllCount())
			.expectNext(10L)
			.verifyComplete();
	}

	@Test
	void update() {
		Recruit recruit = Recruit.builder()
			.title("title")
			.index(1)
			.company("company")
			.build();

		Recruit recruit2 = Recruit.builder()
			.title("title")
			.build();

		Mockito.when(recruitRepository.findByIndexAndCompany(Mockito.anyLong(), Mockito.anyString()))
			.thenReturn(Mono.just(recruit));

		Mockito.when(recruitRepository.save(Mockito.eq(recruit)))
			.thenReturn(Mono.just(recruit2));

		StepVerifier.create(recruitService.update(recruit))
			.expectNext(recruit2)
			.verifyComplete();
	}

	@Test
	void getRecruitsBoolean() {
		Recruit recruit = Recruit.builder().title("title").build();

		Flux<Recruit> recruitFlux = Flux.just(recruit);

		Mockito.when(recruitRepository.findAllByFavorite(Mockito.anyBoolean()))
			.thenReturn(recruitFlux);

		StepVerifier.create(recruitService.getRecruits(true))
			.expectNext(recruit)
			.verifyComplete();
	}

	@Test
	void deleteAll() {
		Mockito.when(recruitRepository.deleteAll())
			.thenReturn(Mono.empty());

		StepVerifier.create(recruitService.deleteAll())
			.verifyComplete();
	}

	@Test
	void saveAll() {
		Recruit recruit = Recruit.builder().title("title").build();

		Flux<Recruit> recruitFlux = Flux.just(recruit);

		Mockito.when(recruitRepository.saveAll(Mockito.anyList()))
			.thenReturn(recruitFlux);

		StepVerifier.create(recruitService.saveAll(List.of()))
			.expectNext(recruit)
			.verifyComplete();
	}
}