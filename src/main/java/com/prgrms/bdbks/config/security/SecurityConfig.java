package com.prgrms.bdbks.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.config.jwt.ExceptionHandlingFilter;
import com.prgrms.bdbks.domain.user.jwt.JwtAccessDeniedHandler;
import com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationEntryPoint;
import com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter;
import com.prgrms.bdbks.domain.user.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final TokenProvider tokenProvider;

	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	private final ObjectMapper objectMapper;

	private final String[] allowedApiUrls = {
		"/api/v1/auth/signup", "/api/v1/auth/login", "/api/v1/categories", "/api/v1/items/**",
		"/api/v1/items", "/docs", "/docs/index.html", "/docs/**"
	};

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.antMatchers(allowedApiUrls);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
		return http
			.csrf().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers(allowedApiUrls).permitAll()
			.anyRequest().authenticated()
			.and()

			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint())
			.accessDeniedHandler(jwtAccessDeniedHandler)
			.and()

			.addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new ExceptionHandlingFilter(objectMapper), JwtAuthenticationFilter.class)
			.build();
	}

}