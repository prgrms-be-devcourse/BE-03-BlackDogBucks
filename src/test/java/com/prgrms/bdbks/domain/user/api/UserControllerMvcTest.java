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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.common.exception.DuplicateInsertException;
import com.prgrms.bdbks.config.jwt.JwtConfigure;
import com.prgrms.bdbks.config.security.SecurityConfig;
import com.prgrms.bdbks.config.web.WebConfig;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.JwtAccessDeniedHandler;
import com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter;
import com.prgrms.bdbks.domain.user.jwt.TokenProvider;
import com.prgrms.bdbks.domain.user.service.AuthService;
import com.prgrms.bdbks.domain.user.service.UserService;

@EnableConfigurationProperties(JwtConfigure.class)
@ConfigurationPropertiesScan(value = {"com.prgrms.bdbks.config"},
	basePackageClasses = {JwtConfigure.class})
@AutoConfigureRestDocs
@Import({SecurityConfig.class, TokenProvider.class, WebConfig.class, JwtAccessDeniedHandler.class})
@WebMvcTest(controllers = {AuthController.class, UserController.class}, properties = {
	"spring.config.location=classpath:application-test.yml"})
@ActiveProfiles("test")
@Disabled
class UserControllerMvcTest {

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

	private String testToken;

	@Autowired
	private JwtConfigure jwtConfigure;

	@BeforeEach
	@Rollback(value = false)
	public void setUp() {

		class EmptyUser extends User {
		}
		emptyUser = new EmptyUser();

		User user = User.builder()
			.loginId(UserObjectProvider.BLACK_DOG_LOGIN_ID)
			.password(UserObjectProvider.BLACK_DOG_PASSWORD)
			.phone("01012345667")
			.nickname(UserObjectProvider.BLACK_DOG_LOGIN_ID)
			.birthDate(UserObjectProvider.BLACK_DOG_BIRTH_DATE)
			.email("qwerqwer@naver.com")
			.build();

		testToken = tokenProvider.generateToken(user);

	}

	@DisplayName("등록 - 사용자 가입에 성공하고 201 코드를 리턴한다.")
	@Test
	void signup_success() throws Exception {

		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andDo(print())
			.andDo(document("signup"))
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@DisplayName("실패 - 중복된 아이디로 사용자 가입에 실패한다.")
	@Test
	void signup_failure() throws Exception {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();

		when(defaultUserService.findUser(userCreateRequest.getLoginId())).thenThrow(DuplicateInsertException.class);

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andExpect(status().isBadRequest());

		verify(defaultUserService).findUser(userCreateRequest.getLoginId());
	}

	@DisplayName("로그인 - 로그인에 성공한다.")
	@Test
	void login_success() throws Exception {
		UserLoginRequest request = UserObjectProvider.createBlackDogLoginRequest();

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
		when(defaultUserService.login(request)).thenReturn(tokenResponse);

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
		UserLoginRequest request = UserObjectProvider.createBlackDogLoginRequest();

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
		UserLoginRequest request = new UserLoginRequest(UserObjectProvider.BLACK_DOG_LOGIN_ID,
			UserObjectProvider.BLACK_DOG_PASSWORD + "123");

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

		when(defaultUserService.findUser(UserObjectProvider.BLACK_DOG_LOGIN_ID)).thenReturn(Optional.of(emptyUser));

		when(userMapper.entityToFindResponse(emptyUser)).thenReturn(findResponse);

		mockMvc.perform(get("/api/v1/users/{loginId}", UserObjectProvider.BLACK_DOG_LOGIN_ID)
				.header(HttpHeaders.AUTHORIZATION, JwtAuthenticationFilter.AUTHENTICATION_TYPE_PREFIX + testToken)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(findResponse)));

		verify(defaultUserService).findUser(UserObjectProvider.BLACK_DOG_LOGIN_ID);
	}

	@DisplayName("조회 - 존재하지 않는 유저는 404코드를 리턴한다.")
	@Test
	void find_failure() throws Exception {
		when(defaultUserService.findUser(UserObjectProvider.BLACK_DOG_LOGIN_ID)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/v1/users/" + UserObjectProvider.BLACK_DOG_LOGIN_ID)).andExpect(status().isNotFound());
	}

}
