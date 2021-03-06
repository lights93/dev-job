package com.mino.devjob.book.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.mino.devjob.book.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, Long> {
	Flux<Book> findAllByFavoriteIsLessThan(int favorite);

	Mono<Boolean> existsByIdAndFavoriteIsNot(Long id, int favorite);
}
