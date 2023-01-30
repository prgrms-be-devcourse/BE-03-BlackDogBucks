package com.prgrms.bdbks.domain.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
	void setup() throws Exception {
		UserCreateRequest userCreateRequest = new UserCreateRequest("user123", "password", "nickname",
			LocalDate.now().minusYears(10L),
			"01012341234", "user@naver.com");
		ResponseEntity<?> responseEntity = userController.signup(userCreateRequest);

		String loginId = "user123";
		String password = "password";

		UserLoginRequest userLoginRequest = new UserLoginRequest(loginId, password);

		ResponseEntity<TokenResponse> result = userController.login(userLoginRequest);

		MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userLoginRequest)))
			.andExpect(status().isOk())
			.andReturn();

		token = mvcResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
	}

	@Test
	void test() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}
}
