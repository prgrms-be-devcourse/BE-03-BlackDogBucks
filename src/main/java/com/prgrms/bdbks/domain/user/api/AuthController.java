package com.prgrms.bdbks.domain.user.api;

import static com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter.*;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	/**
	 * <pre>
	 *     회원 가입
	 * </pre>
	 * @param userCreateRequest - 등록할 회원의 정보
	 * @return status : created , body : URI
	 */
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<URI> signup(@RequestBody @Valid UserCreateRequest userCreateRequest) {
		userService.register(userCreateRequest);
		return ResponseEntity.created(URI.create("/api/v1/auth/login")).build();
	}

	/**
	 * <pre>
	 *     로그인
	 * </pre>
	 * @param loginRequest - 로그인 요청 정보(loginId, PW)
	 * @return status : ok , body : TokenResponse
	 */
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenResponse> login(@RequestBody @Valid UserLoginRequest loginRequest,
		HttpServletResponse response) {

		TokenResponse tokenResponse = userService.login(loginRequest);

		response.addHeader(HttpHeaders.AUTHORIZATION, AUTHENTICATION_TYPE_PREFIX + tokenResponse.getToken());

		return ResponseEntity.ok(tokenResponse);
	}

}