package com.mino.devjob.service;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mino.devjob.model.KakaoRecruit;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class CrawlKakaoServiceTest {

	@InjectMocks
	private CrawlKakaoService crawlKakao;

	@Test
	void crawl() throws IOException {
		Flux<KakaoRecruit> kakaoRecruits = crawlKakao.crawl();

		//TODO 뭐를 해야하지..
	}
}