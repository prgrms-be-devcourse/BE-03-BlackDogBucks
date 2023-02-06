package com.prgrms.bdbks.domain.user.api;

import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.WithMockCustomUser;
import com.prgrms.bdbks.WithMockCustomUserSecurityContextFactory;
import com.prgrms.bdbks.domain.testutil.StoreObjectProvider;
import com.prgrms.bdbks.domain.user.dto.StoreUserChangeRequest;
import com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter;
import com.prgrms.bdbks.domain.user.role.Role;

import lombok.RequiredArgsConstructor;

@Transactional
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest(properties = {"spring.profiles.active=test"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Sql({"/test_sql/user.sql"})
class UserControllerIntegrationTest {

	private final MockMvc mockMvc;

	private final ObjectMapper objectMapper;

	@DisplayName("me() - 인증 완료 후 개인정보 조회에 성공한다.")
	@Test
	@WithMockCustomUser(username = "tinajeong", role = "ADMIN")
	void me_ValidParameters_Success() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX + WithMockCustomUserSecurityContextFactory.token)
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
	@WithMockCustomUser
	void readUser_success() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + BLACK_DOG_LOGIN_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX + WithMockCustomUserSecurityContextFactory.token)
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

	@DisplayName("changeStoreUser() - 인증 완료 후 유저의 매장 정보 변경에 성공한다.")
	@Test
	@WithMockCustomUser
	void changeStoreUserInformation_ValidParameters_Success() throws Exception {

		StoreUserChangeRequest storeUserChangeRequest = new StoreUserChangeRequest(BLACK_DOG_LOGIN_ID, Role.ROLE_ADMIN,
			StoreObjectProvider.STORE_ID);

		mockMvc.perform(patch("/api/v1/users/store")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION,
					JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX + WithMockCustomUserSecurityContextFactory.token)
				.content(objectMapper.writeValueAsString(storeUserChangeRequest))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("User's Store Information Modified"))

			.andDo(document("change-user-store",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("role").type(JsonFieldType.STRING).description("변경할 권한"),
					fieldWithPath("storeId").type(JsonFieldType.STRING).description("매장 ID")
				)
			));
	}
}
