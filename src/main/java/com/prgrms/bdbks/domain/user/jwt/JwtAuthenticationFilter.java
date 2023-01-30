package com.prgrms.bdbks.domain.user.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.prgrms.bdbks.common.exception.JwtValidateException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;

	public static final String AUTHENTICATION_TYPE_PREFIX = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
		FilterChain filterChain) throws ServletException, IOException {
		String jwt = resolveToken(httpServletRequest);

		if (tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} else {
			String requestURI = httpServletRequest.getRequestURI();
			log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		return Optional.of(bearerToken)
			.filter(t -> StringUtils.hasText(bearerToken))
			.filter(t -> t.startsWith(AUTHENTICATION_TYPE_PREFIX))
			.map(t -> t.substring(7))
			.orElseThrow(() ->
				new JwtValidateException("유효한 형식의 JWT 토큰이 아닙니다."));
	}

}
