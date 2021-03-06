package com.mino.devjob.recruit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mino.devjob.recruit.dto.NaverRecruitDto;
import com.mino.devjob.recruit.model.Recruit;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@Service("NAVER")
@Slf4j
public class CrawlNaverService implements CrawlService {
	private final WebClient webClient;

	@SneakyThrows
	@Autowired
	public CrawlNaverService(WebClient webClient) {
		// https://stackoverflow.com/questions/45418523/spring-5-webclient-using-ssl
		SslContext sslContext = SslContextBuilder
			.forClient()
			.trustManager(InsecureTrustManagerFactory.INSTANCE)
			.build();

		HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
		ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);

		this.webClient = webClient.mutate()
			.baseUrl("https://recruit.navercorp.com")
			.clientConnector(httpConnector)
			.build();
	}

	@Override
	public Flux<Recruit> crawl() {
		return getNaverRecruits()
			.map(NaverRecruitDto::toRecruit);
	}

	private Flux<NaverRecruitDto> getNaverRecruits() {
		return webClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.pathSegment("naver", "job", "listJson")
				.queryParam("classNm", "developer")
				.queryParam("startNum", "1")
				.queryParam("endNum", "300")
				.build())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.retrieve()
			.bodyToFlux(NaverRecruitDto.class)
			.onErrorResume(error -> {
				log.error("get Naver error!!", error);
				return Flux.empty();
			});
	}
}
