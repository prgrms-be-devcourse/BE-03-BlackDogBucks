package com.prgrms.bdbks.domain.user.api;

import static com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter.*;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.TokenProvider;
import com.prgrms.bdbks.domain.user.service.AuthService;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final TokenProvider tokenProvider;

	private final UserService userService;

	private final AuthService authService;

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	/**
	 * <pre>
	 *     회원 가입
	 * </pre>
	 * @param userCreateRequest - 등록할 회원의 정보
	 * @return status : created , body : URI
	 */
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<URI> signup(@Valid @RequestBody UserCreateRequest userCreateRequest) {
		if (userService.findUser(userCreateRequest.getLoginId()).isPresent()) {
			return new ResponseEntity<>(URI.create("/signup"), HttpStatus.BAD_REQUEST);
		}

		userService.register(userCreateRequest);
		return ResponseEntity.created(URI.create("/login")).build();

	}

	/**
	 * <pre>
	 *     로그아웃
	 * </pre>
	 * @param
	 * @return status :  , body :
	 */
	@PostMapping(value = {"/logout"})
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.noContent().build();
	}

	/**
	 * <pre>
	 *     로그인
	 * </pre>
	 * @param loginRequest - 로그인 요청 정보(loginId, PW)
	 * @return status : ok , body : TokenResponse
	 */
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginRequest loginRequest) {

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = userService.findUser(loginRequest.getLoginId())
			.orElseThrow(() -> new UsernameNotFoundException("잘못된 사용자 정보입니다."));

		String jwt = tokenProvider.generateToken(user);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.AUTHORIZATION, AUTHENTICATION_TYPE_PREFIX + jwt);

		return new ResponseEntity<>(new TokenResponse(jwt), httpHeaders, HttpStatus.OK);
	}
}