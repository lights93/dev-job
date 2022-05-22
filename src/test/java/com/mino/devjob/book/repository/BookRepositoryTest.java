package com.mino.devjob.book.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mino.devjob.book.model.Book;

import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class BookRepositoryTest {
	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void setUp() {
		Book favorite = Book.builder()
			.id(1L)
			.favorite(1)
			.build();

		bookRepository.save(favorite).block();
	}

	@DisplayName("id가 일치하지만, favorite이 일치하지 않는 것 존재 확인")
	@Test
	void existsByIdAndFavoriteIsTrue() {
		StepVerifier.create(bookRepository.existsByIdAndFavoriteIsNot(1L, 0))
			.expectNext(true)
			.verifyComplete();
	}

	@DisplayName("id가 일치하지만, favorite이 일치하지 않는 것 존재하지 않음 확인")
	@ParameterizedTest
	@CsvSource(value = {"2,0", "1,1", "2,1"})
	void existsByIdAndFavoriteIsFalse(long id, int favorite) {
		StepVerifier.create(bookRepository.existsByIdAndFavoriteIsNot(id, favorite))
			.expectNext(false)
			.verifyComplete();
	}
}