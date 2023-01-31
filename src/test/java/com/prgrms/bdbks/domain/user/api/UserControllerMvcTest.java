package com.prgrms.bdbks.domain.user.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.config.jwt.JwtConfigure;
import com.prgrms.bdbks.config.security.SecurityConfig;
import com.prgrms.bdbks.config.web.WebConfig;
import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.TokenProvider;
import com.prgrms.bdbks.domain.user.role.Role;
import com.prgrms.bdbks.domain.user.service.AuthService;
import com.prgrms.bdbks.domain.user.service.UserService;

@EnableConfigurationProperties(JwtConfigure.class)
@ConfigurationPropertiesScan(value = {"com.prgrms.bdbks.config"},
	basePackageClasses = {JwtConfigure.class})
@AutoConfigureRestDocs
@Import({SecurityConfig.class, TokenProvider.class, WebConfig.class})
@WebMvcTest(controllers = {AuthController.class, UserController.class}, properties = {
	"spring.config.location=classpath:application-test.yml"})
@ActiveProfiles("test")
class UserControllerMvcTest {

	private static final String USER_API_PATH = "/api/v1/users/";
	// private final String AUTH_API_PATH = "/api/v1/auth/";

	private static final String USER_LOGIN_ID = "blackdog";

	private static final String USER_PASSWORD = "password";

	private static final LocalDate USER_BIRTH_DATE = LocalDate.now();

	private static final Role USER_ROLE = Role.ROLE_USER;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private User emptyUser;

	@MockBean
	private UserService defaultUserService;

	@MockBean
	private UserMapper userMapper;

	@MockBean
	private AuthService authService;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@MockBean
	private AuthenticationManager authenticationManager;

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
			USER_BIRTH_DATE, USER_PHONE, USER_EMAIL);

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andDo(print())
			.andDo(document("signup"))
			.andExpect(status().isCreated())
			// .andExpect(content().string("Sign Up Completed"))
			.andDo(print());
	}

	@DisplayName("실패 - 중복된 아이디로 사용자 가입에 실패한다.")
	@Test
	void signup_failure() throws Exception {
		String USER_NAME = "blackdog";
		String USER_PHONE = "01012341234";
		String USER_EMAIL = "blackdog@blackdog.com";
		UserCreateRequest userCreateRequest = new UserCreateRequest(USER_LOGIN_ID, USER_PASSWORD, USER_NAME,
			USER_BIRTH_DATE, USER_PHONE, USER_EMAIL);

		when(defaultUserService.findUser(userCreateRequest.getLoginId())).thenReturn(Optional.of(emptyUser));

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andExpect(status().isBadRequest());

		verify(defaultUserService).findUser(userCreateRequest.getLoginId());
	}

	@DisplayName("로그인 - 로그인에 성공한다.")
	@Test
	void login_success() throws Exception {
		UserLoginRequest request = new UserLoginRequest(USER_LOGIN_ID, USER_PASSWORD);

		UsernamePasswordAuthenticationToken beforeAuthenticate =
			new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

		UsernamePasswordAuthenticationToken afterAuthenticate =
			new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword(),
				List.of(new SimpleGrantedAuthority("USER")));

		when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

		when(authenticationManager.authenticate(beforeAuthenticate)).thenReturn(afterAuthenticate);

		when(defaultUserService.findUser(request.getLoginId())).thenReturn(Optional.of(emptyUser));

		String token = "tokenValue12345";

		when(tokenProvider.generateToken(emptyUser))
			.thenReturn(token);

		TokenResponse tokenResponse = new TokenResponse(token);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(tokenResponse)))
			.andDo(document("login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				)
			));

		verify(authenticationManagerBuilder).getObject();

		verify(authenticationManager).authenticate(beforeAuthenticate);

		verify(defaultUserService).findUser(request.getLoginId());

		verify(tokenProvider).generateToken(emptyUser);
	}

	@DisplayName("로그인 - 기존에 가입된 ID가 없는 경우 로그인에 실패한다.")
	@Test
	void login_failure_notRegistered() throws Exception {
		UserLoginRequest request = new UserLoginRequest(USER_LOGIN_ID, USER_PASSWORD);

		UsernamePasswordAuthenticationToken beforeAuthenticate =
			new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

		UsernamePasswordAuthenticationToken afterAuthenticate =
			new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword(),
				List.of(new SimpleGrantedAuthority("USER")));

		when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

		when(authenticationManager.authenticate(beforeAuthenticate)).thenReturn(afterAuthenticate);

		when(defaultUserService.findUser(request.getLoginId())).thenReturn(Optional.empty());

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameNotFoundException));

		verify(authenticationManagerBuilder).getObject();

		verify(authenticationManager).authenticate(beforeAuthenticate);

		verify(defaultUserService).findUser(request.getLoginId());
	}

	@DisplayName("로그인 - ID는 일치하나, 비밀번호가 일치하지 않는 경우 로그인에 실패한다.")
	@Test
	void login_failure_invalidatePassword() throws Exception {
		UserLoginRequest request = new UserLoginRequest(USER_LOGIN_ID, USER_PASSWORD + "123");

		UsernamePasswordAuthenticationToken beforeAuthenticate =
			new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

		when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

		when(authenticationManager.authenticate(beforeAuthenticate)).thenThrow(BadCredentialsException.class);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof BadCredentialsException));

		verify(authenticationManagerBuilder).getObject();

		verify(authenticationManager).authenticate(beforeAuthenticate);
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
