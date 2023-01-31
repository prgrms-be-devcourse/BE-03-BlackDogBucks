package com.prgrms.bdbks.domain.testutil;

import java.time.LocalDateTime;

import com.prgrms.bdbks.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserObjectProvider {

	public static User createUser() {
		return User.builder()
			.birthDate(LocalDateTime.now().minusYears(26L).toLocalDate())
			.email("test@naver.com")
			.loginId("test1234")
			.password("password1234")
			.nickname("이디야화이팅")
			.phone("01012341234")
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
			.phone("01012341234")
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

}