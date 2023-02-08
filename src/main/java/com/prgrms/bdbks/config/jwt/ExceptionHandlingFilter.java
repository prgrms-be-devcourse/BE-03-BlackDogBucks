package com.prgrms.bdbks.config.jwt;

import static org.apache.commons.lang3.CharEncoding.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.common.dto.ErrorResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExceptionHandlingFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			ResponseEntity<ErrorResponse> responseEntity = errorResponse(request, e);

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(UTF_8);
			response.setStatus(responseEntity.getStatusCodeValue());
			response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
		}

	}

	private ResponseEntity<ErrorResponse> errorResponse(HttpServletRequest request, Exception e) {
		ErrorResponse errorResponse = ErrorResponse.unAuthorized(e.getMessage(), request.getRequestURI(), null);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}
}
