package com.prgrms.bdbks.domain.user.api;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.config.security.SecurityConfig;
import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;
import com.prgrms.bdbks.domain.user.role.Role;
import com.prgrms.bdbks.domain.user.service.UserService;

@AutoConfigureRestDocs
@Import(SecurityConfig.class)
@WebMvcTest({AuthController.class, UserController.class})
class UserControllerMvcTest {

	private final String USER_API_PATH = "/api/v1/users/";
	private final String AUTH_API_PATH = "/api/v1/auth/";

	private final String USER_LOGIN_ID = "blackdog";

	private final String USER_PASSWORD = "password";

	private final LocalDate USER_BIRTH_DATE = LocalDate.now();

	private final Role USER_ROLE = Role.USER;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private User emptyUser;

	@MockBean
	private UserService defaultUserService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserMapper userMapper;

	@MockBean
	private HttpSession session;

	@BeforeEach
	public void setUp() {
		class EmptyUser extends User {
		}
		emptyUser = new EmptyUser();
	}

	@DisplayName("등록 - 사용자 가입에 성공하고 201 코드를 리턴한다.")
	@Test
	void signup_success() throws Exception {
		String USER_NAME = "blackdog";
		String USER_PHONE = "01012341234";
		String USER_EMAIL = "blackdog@blackdog.com";
		UserCreateRequest userCreateRequest = new UserCreateRequest(USER_LOGIN_ID, USER_PASSWORD, USER_NAME,
			USER_BIRTH_DATE, USER_PHONE, USER_EMAIL, USER_ROLE);

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andDo(print())
			.andDo(document("signup"))
			.andExpect(status().isCreated())
			.andExpect(content().string("Sign Up Completed"))
			.andDo(print());
	}

	@DisplayName("실패 - 중복된 아이디로 사용자 가입에 실패한다.")
	@Test
	void signup_failure() throws Exception {
		String USER_NAME = "blackdog";
		String USER_PHONE = "01012341234";
		String USER_EMAIL = "blackdog@blackdog.com";
		UserCreateRequest userCreateRequest = new UserCreateRequest(USER_LOGIN_ID, USER_PASSWORD, USER_NAME,
			USER_BIRTH_DATE, USER_PHONE, USER_EMAIL, USER_ROLE);

		when(defaultUserService.findUser(userCreateRequest.getLoginId())).thenReturn(Optional.of(emptyUser));

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andExpect(status().isBadRequest());
	}

	@DisplayName("조회 - 로그인에 성공한다.")
	@Test
	void login_success() throws Exception {
		UserLoginRequest request = new UserLoginRequest(USER_LOGIN_ID, USER_PASSWORD);

		when(defaultUserService.login(request.getLoginId(), request.getPassword()))
			.thenReturn(Optional.of(emptyUser));

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.sessionAttr("user", emptyUser)
			)
			.andDo(print())
			.andDo(document("login"))
			.andExpect(status().isOk())
			.andExpect(content().string("Login Succeeded"));

		verify(defaultUserService).login(USER_LOGIN_ID, USER_PASSWORD);
	}

	@DisplayName("조회 - 로그인에 실패한다.")
	@Test
	void login_failure() throws Exception {
		UserLoginRequest request = new UserLoginRequest(USER_LOGIN_ID, USER_PASSWORD);

		when(defaultUserService.login(request.getLoginId(), request.getPassword())).thenReturn(Optional.empty());

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.sessionAttr("user", emptyUser)
			)
			.andExpect(status().isUnauthorized())
			.andExpect(content().string("Login Failed"));

		verify(defaultUserService).login(USER_LOGIN_ID, USER_PASSWORD);
	}

	@DisplayName("조회 - 존재하는 유저라면 200 코드와 유저 정보를 얻는데 성공한다.")
	@Test
	void find_success() throws Exception {

		UserFindResponse findResponse = UserFindResponse.builder().build();

		when(defaultUserService.findUser(USER_LOGIN_ID)).thenReturn(Optional.of(emptyUser));
		when(userMapper.entityToFindResponse(emptyUser)).thenReturn(findResponse);

		mockMvc.perform(get("/api/v1/users/{loginId}", USER_LOGIN_ID))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(findResponse)));

		verify(defaultUserService).findUser(USER_LOGIN_ID);
	}

	@DisplayName("조회 - 존재하지 않는 유저는 404코드를 리턴한다.")
	@Test
	void find_failure() throws Exception {
		when(defaultUserService.findUser(USER_LOGIN_ID)).thenReturn(Optional.empty());
		mockMvc.perform(get(USER_API_PATH + USER_LOGIN_ID)).andExpect(status().isNotFound());
	}

}
