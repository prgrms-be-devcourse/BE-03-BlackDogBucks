package com.prgrms.bdbks.domain.testutil;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserObjectProvider {

	public static final String BLACK_DOG_LOGIN_ID = "blackdog";

	public static final String BLACK_DOG_PASSWORD = "blackdog123";

	public static final LocalDate BLACK_DOG_BIRTH_DATE = LocalDate.now().minusYears(10L);

	public static User createUser() {
		return User.builder()
			.birthDate(LocalDateTime.now().minusYears(26L).toLocalDate())
			.email("test@naver.com")
			.loginId("test1234")
			.password("password1234")
			.nickname("이디야화이팅")
			.phone("01019879874")
			.build();
	}

	public static User createUser(Long id) {
		return User.builder()
			.id(id)
			.birthDate(LocalDateTime.now().minusYears(26L).toLocalDate())
			.email("test@naver.com")
			.loginId("test1234")
			.password("password1234")
			.nickname("이디야화이팅")
			.phone("01019879874")
			.build();
	}

	public static User createUser(String loginId, String email, String phone) {
		return User.builder()
			.birthDate(LocalDateTime.now().minusYears(26L).toLocalDate())
			.email(email)
			.loginId(loginId)
			.password("password1234")
			.nickname("이디야화이팅")
			.phone(phone)
			.build();
	}

	public static UserCreateRequest createBlackDogRequest() {
		return new UserCreateRequest(BLACK_DOG_LOGIN_ID,
			BLACK_DOG_PASSWORD,
			BLACK_DOG_LOGIN_ID,
			BLACK_DOG_BIRTH_DATE,
			"01098541234",
			"blackdog@blackdog.com");
	}

	public static UserCreateRequest createMockUserRequest(String username) {
		return new UserCreateRequest(username,
			username + "password",
			username,
			BLACK_DOG_BIRTH_DATE,
			"01098541234",
			username + "@blackdog.com");
	}

	public static UserLoginRequest createBlackDogLoginRequest() {
		return new UserLoginRequest(BLACK_DOG_LOGIN_ID, BLACK_DOG_PASSWORD);
	}

}