package com.mino.devjob.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

import lombok.SneakyThrows;

/**
 * https://medium.com/@kevin_park/springboot-oauth2-0-reative-client-with-spring-security-a30fe3f7e386
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity serverHttpSecurity) {

		return serverHttpSecurity
			.csrf().disable()
			.httpBasic().disable()
			.formLogin().disable()
			.authorizeExchange()
			.pathMatchers("/api/**").authenticated()
			.anyExchange().permitAll()
			.and().oauth2Login().authenticationSuccessHandler(redirectServerAuthenticationSuccessHandler())
			.and().logout().logoutSuccessHandler(redirectServerLogoutSuccessHandler())
			.and().exceptionHandling()
			.and().build();
	}

	@SneakyThrows
	public RedirectServerAuthenticationSuccessHandler redirectServerAuthenticationSuccessHandler() {
		return new RedirectServerAuthenticationSuccessHandler("http://localhost:8080");
	}

	public RedirectServerLogoutSuccessHandler redirectServerLogoutSuccessHandler() {
		RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
		logoutSuccessHandler.setLogoutSuccessUrl(URI.create("http://localhost:8080"));

		return logoutSuccessHandler;
	}

}
