package com.prgrms.bdbks.domain.user.api;

import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserAuthChangeRequest;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.role.Role;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Transactional
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest(properties = {"spring.profiles.active=test"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class UserControllerIntegrationTest {

	private final MockMvc mockMvc;

	private final ObjectMapper objectMapper;

	private final UserService userService;

	private final UserMapper userMapper;

	private String token;

	private UserFindResponse userFindResponse;

	@BeforeEach
	void setup() {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();
		userService.register(userCreateRequest);

		UserLoginRequest userLoginRequest = UserObjectProvider.createBlackDogLoginRequest();
		TokenResponse tokenResponse = userService.login(userLoginRequest);

		User user = userService.findUser(BLACK_DOG_LOGIN_ID).get();
		userFindResponse = userMapper.entityToFindResponse(user);

		token = "Bearer " + tokenResponse.getToken();
	}

	@DisplayName("me() - 인증 완료 후 개인정보 조회에 성공한다.")
	@Test
	void me_ValidParameters_Success() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
			)
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("me",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
					fieldWithPath("birthDate").type(JsonFieldType.ARRAY).description("생년월일"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호"),
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
				)
			));
	}

	@DisplayName("readUser() - 인증 완료 후 회원정보를 조회하는데 성공한다.")
	@Test
	void readUser_success() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + BLACK_DOG_LOGIN_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
			)
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("find-user",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),

				responseFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
					fieldWithPath("birthDate").type(JsonFieldType.ARRAY).description("생년월일"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("핸드폰 번호"),
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
				)
			));
	}

	@DisplayName("changeUserAuthority() - 인증 완료 후 권한 변경에 성공한다.")
	@Test
	void changeUserAuthority_ValidParameters_Success() throws Exception {

		UserAuthChangeRequest userAuthChangeRequest = new UserAuthChangeRequest(BLACK_DOG_LOGIN_ID, Role.ROLE_ADMIN);

		mockMvc.perform(patch("/api/v1/users/authority")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
				.content(objectMapper.writeValueAsString(userAuthChangeRequest))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("User Authority Modified"))

			.andDo(document("change-authority",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("role").type(JsonFieldType.STRING).description("변경할 권한")
				)
			));
	}
}
