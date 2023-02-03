package com.prgrms.bdbks.domain.user.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@AutoConfigureRestDocs
@Transactional
@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Sql({"/test_sql/user.sql"})
public class AuthControllerIntegrationTest {

	@Autowired
	private final MockMvc mockMvc;

	@Autowired
	private final ObjectMapper objectMapper;

	private final AuthController authController;

	private final UserService userService;

	private String token;

	private UserLoginRequest userLoginRequest;

	@BeforeEach
	void setup() {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();
		authController.signup(userCreateRequest);

		userLoginRequest = UserObjectProvider.createBlackDogLoginRequest();
		TokenResponse tokenResponse = userService.login(userLoginRequest);
		token = "Bearer " + tokenResponse.getToken();
	}

	@DisplayName("signup() - 입력한 정보가 올바른 경우 회원가입에 성공한다.")
	@Test
	void signup_success() throws Exception {
		UserCreateRequest createRequest = new UserCreateRequest("LoginId123", "password123", "nickname123",
			LocalDate.now().minusYears(10L),
			"01012345678", "user@naver.com");

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("/api/v1/auth/login")))
			.andDo(print())
			.andDo(document("signup",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
					fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호"),
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
				)
			));
	}

	@DisplayName("login() - 아이디와 비밀번호가 DB의 값과 일치할 경우 로그인에 성공한다.")
	@Test
	void login_success() throws Exception {

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userLoginRequest)))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("token").type(JsonFieldType.STRING).description("토큰 값")
				)
			));
	}
}
