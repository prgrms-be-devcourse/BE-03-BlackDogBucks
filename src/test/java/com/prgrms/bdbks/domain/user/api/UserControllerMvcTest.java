package com.prgrms.bdbks.domain.user.api;

import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static com.prgrms.bdbks.domain.user.jwt.JwtAuthenticationFilter.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.common.exception.DuplicateInsertException;
import com.prgrms.bdbks.domain.testutil.StoreObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.converter.UserMapper;
import com.prgrms.bdbks.domain.user.dto.StoreUserChangeRequest;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.TokenProvider;
import com.prgrms.bdbks.domain.user.role.Role;
import com.prgrms.bdbks.domain.user.service.UserService;

@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(controllers = {AuthController.class, UserController.class})
class UserControllerMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService defaultUserService;

	@MockBean
	private UserMapper userMapper;

	@MockBean
	private TokenProvider tokenProvider;

	private User emptyUser;

	private String testToken;

	static class EmptyUser extends User {
	}

	@BeforeEach
	@Rollback(value = false)
	public void setUp() {

		emptyUser = new EmptyUser();

		User user = User.builder()
			.loginId(BLACK_DOG_LOGIN_ID)
			.password(UserObjectProvider.BLACK_DOG_PASSWORD)
			.phone("01012345667")
			.nickname(BLACK_DOG_LOGIN_ID)
			.birthDate(UserObjectProvider.BLACK_DOG_BIRTH_DATE)
			.email("qwerqwer@naver.com")
			.build();

		testToken = tokenProvider.generateToken(user);
	}

	@DisplayName("?????? - ????????? ????????? ???????????? 201 ????????? ????????????.")
	@Test
	void signup_success() throws Exception {

		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreateRequest))
				.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@DisplayName("?????? - ????????? ???????????? ????????? ????????? ????????????.")
	@Test
	void signup_failure() throws Exception {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();

		when(defaultUserService.register(any())).thenThrow(DuplicateInsertException.class);

		mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
				.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andExpect(status().isBadRequest());

		verify(defaultUserService).register(any());
	}

	@DisplayName("????????? - ???????????? ????????????.")
	@Test
	void login_success() throws Exception {
		UserLoginRequest request = UserObjectProvider.createBlackDogLoginRequest();

		TokenResponse tokenResponse = new TokenResponse("tokenValue");

		when(defaultUserService.login(any(UserLoginRequest.class))).thenReturn(tokenResponse);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf())
			)
			.andDo(print())
			.andExpect(header().string(HttpHeaders.AUTHORIZATION, AUTHENTICATION_TYPE_PREFIX + "tokenValue"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(tokenResponse)));

		verify(defaultUserService).login(any(UserLoginRequest.class));
	}

	@DisplayName("????????? - ????????? ????????? ID??? ?????? ?????? ???????????? ????????????.")
	@Test
	void login_failure_notRegistered() throws Exception {
		UserLoginRequest request = UserObjectProvider.createBlackDogLoginRequest();

		when(defaultUserService.login(any())).thenThrow(UsernameNotFoundException.class);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf())
			)
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameNotFoundException));

		verify(defaultUserService).login(any());
	}

	@DisplayName("????????? - ID??? ????????????, ??????????????? ???????????? ?????? ?????? ???????????? ????????????.")
	@Test
	void login_failure_invalidatePassword() throws Exception {
		UserLoginRequest request = new UserLoginRequest(BLACK_DOG_LOGIN_ID,
			UserObjectProvider.BLACK_DOG_PASSWORD + 123);

		when(defaultUserService.login(any())).thenThrow(BadCredentialsException.class);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf())
			)
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof BadCredentialsException));

		verify(defaultUserService).login(any());
	}

	@DisplayName("?????? - ???????????? ???????????? 200 ????????? ?????? ????????? ????????? ????????????.")
	@Test
	void find_success() throws Exception {

		UserFindResponse findResponse = UserFindResponse.builder().build();

		when(defaultUserService.findUser(BLACK_DOG_LOGIN_ID)).thenReturn(Optional.of(emptyUser));

		when(userMapper.entityToFindResponse(emptyUser)).thenReturn(findResponse);

		mockMvc.perform(get("/api/v1/users/{loginId}", BLACK_DOG_LOGIN_ID)
				.header(HttpHeaders.AUTHORIZATION, AUTHENTICATION_TYPE_PREFIX + testToken)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(findResponse)));

		verify(defaultUserService).findUser(BLACK_DOG_LOGIN_ID);
	}

	@DisplayName("?????? - ???????????? ?????? ????????? 404????????? ????????????.")
	@Test
	void find_failure() throws Exception {
		when(defaultUserService.findUser(BLACK_DOG_LOGIN_ID)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/v1/users/" + BLACK_DOG_LOGIN_ID)).andExpect(status().isNotFound());
	}

	@DisplayName("?????? ?????? - ????????? ?????? ????????? ????????????.")
	@Test
	void changeAuthority_success() throws Exception {

		StoreUserChangeRequest storeUserChangeRequest = new StoreUserChangeRequest(BLACK_DOG_LOGIN_ID, Role.ROLE_ADMIN,
			StoreObjectProvider.STORE_ID);

		doNothing().when(defaultUserService).changeStoreUser(any());

		mockMvc.perform(patch("/api/v1/users/store")
				.header(HttpHeaders.AUTHORIZATION, AUTHENTICATION_TYPE_PREFIX + testToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(storeUserChangeRequest))
				.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("User's Store Information Modified"));

		verify(defaultUserService).changeStoreUser(any());
	}

}
