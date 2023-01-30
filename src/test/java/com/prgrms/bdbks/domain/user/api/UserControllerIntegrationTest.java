package com.prgrms.bdbks.domain.user.api;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class UserControllerIntegrationTest {

	private final MockMvc mockMvc;

	private final AuthController userController;

	private final ObjectMapper objectMapper;

	private String token;

	@BeforeEach
	void setup() {
		UserCreateRequest userCreateRequest = new UserCreateRequest("user123", "pw123", "nickname",
			LocalDate.now().minusYears(10L),
			"01012341234", "user123@naver.com");
		ResponseEntity<?> responseEntity = userController.signup(userCreateRequest);

		String loginId = "user123";
		String password = "pw123";

		UserLoginRequest userLoginRequest = new UserLoginRequest(loginId, password);

		ResponseEntity<TokenResponse> result = userController.login(userLoginRequest);

		token = Objects.requireNonNull(result.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
	}

	@DisplayName("me() - 로그인 후 개인정보 조회에 성공한다.")
	@Test
	void me_ValidParameters_Success() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}
}
