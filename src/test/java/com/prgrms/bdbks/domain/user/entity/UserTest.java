package com.prgrms.bdbks.domain.user.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.user.authority.Authority;

class UserTest {

	private User createUser(String loginId, String password, String nickname, LocalDateTime birthDate, String phone,
		String email, Authority authority) {

		return User.builder()
			.loginId(loginId)
			.password(password)
			.nickname(nickname)
			.birthDate(birthDate)
			.phone(phone)
			.email(email)
			.authority(authority)
			.build();
	}

	private final String validLoginId = "user123";
	private final String validPassword = "password123";
	private final String validNickname = "nickname123";
	private final String validPhone = "01012345678";
	private final String validEmail = "bdbks@naver.com";
	private final Authority validAuthority = Authority.USER;
	private final LocalDateTime validBirthDate = LocalDateTime.now().minusYears(10L);

	@Test
	@DisplayName("Builder - 유저의 모든 필드 값이 유효한 경우 - 생성 성공")
	void builder_validBuilder_Success() {

		assertDoesNotThrow(
			() -> createUser(validLoginId, validPassword, validNickname, validBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	@DisplayName("validateLoginId - 유저의 Id값이 존재하지 않는 경우 - 생성 실패")
	void validateLoginId_EmptyValue_ExceptionThrown(String invalidUserId) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(invalidUserId, validPassword, validNickname, validBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@ValueSource(strings = {"a", "ab", "abcde", "asdfasdasdfasdfasdffasdfasdf"})
	@DisplayName("validateLoginId - 유저의 Id 길이가 제한 길이를 벗어나는 경우 - 생성 실패")
	void validateLoginId_NotValidLength_ExceptionThrown(String invalidUserId) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(invalidUserId, validPassword, validNickname, validBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	@DisplayName("validateLoginId - 유저의 password 값이 존재하지 않는 경우 - 생성 실패")
	void validatePassword_EmptyValue_ExceptionThrown(String invalidPassword) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, invalidPassword, validNickname, validBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@ValueSource(strings = {"a", "ab", "abcde", "1234567", "123456789012341231234578901", "asdfasdasdfasdfaffasdfasdf"})
	@DisplayName("validatePassword - 유저의 password 길이가 제한 길이를 벗어나는 경우 - 생성 실패")
	void validatePassword_NotValidLength_ExceptionThrown(String invalidPassword) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(invalidPassword, invalidPassword, validNickname, validBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	@DisplayName("validateNickname - 유저의 nickname 길이가 제한 길이를 벗어나는 경우 - 생성 실패")
	void validateNickname_EmptyValue_ExceptionThrown(String invalidPassword) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, invalidPassword, validBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@ValueSource(strings = {"a", "ab", "abc", "123", "1234567890123457890123", "asdfasdasdfasdfaffasdfasdf"})
	@DisplayName("validatePassword - 유저의 nickname 길이가 제한 길이를 벗어나는 경우 - 생성 실패")
	void validateNickname_NotValidLength_ExceptionThrown(String invalidNickname) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, invalidNickname, validBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@Test
	@DisplayName("validateBirthDate - 유저의 birthDate 값이 LocalDateTime.now()보다 미래인 경우 - 생성 실패")
	void validateNickname_NotValidLength_ExceptionThrown() {
		LocalDateTime invalidBirthDate = LocalDateTime.now().plusSeconds(1L);

		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, validNickname, invalidBirthDate, validPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", " ", "   ", "   \n"})
	@DisplayName("validatePhone - 유저의 phone 값이 존재하지 않는 경우 - 생성 실패")
	void validatePhone_EmptyValue_ExceptionThrown(String invalidPhone) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, validNickname, validBirthDate, invalidPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@ValueSource(strings = {"010", "0101234", "010123456789", "0101234123412341234"})
	@DisplayName("validatePhone - 유저의 phone 값의 길이가 올바르지 않는 경우 - 생성 실패")
	void validatePhone_NotValidLength_ExceptionThrown(String invalidPhone) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, validNickname, validBirthDate, invalidPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@ValueSource(strings = {"a", "a01b01c23d4", "O1012345678", "010I2345678"})
	@DisplayName("validatePhone - 유저의 phone 값에 문자열이 섞여있는 경우 - 생성 실패")
	void validatePhone_NotDigit_ExceptionThrown(String invalidPhone) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, validNickname, validBirthDate, invalidPhone, validEmail,
				validAuthority));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", " ", "   ", "   \n"})
	@DisplayName("validateEmail - 유저의 email 값이 존재하지 않는 경우 - 생성 실패")
	void validateEmail_EmptyValue_ExceptionThrown(String invalidEmail) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, validNickname, validBirthDate, validPhone, invalidEmail,
				validAuthority));
	}

	@ParameterizedTest
	@ValueSource(strings = {"invalid£@domain.com", "invali\"d@domain.com", "user@"})
	@DisplayName("validateEmail - 유저의 email 형식이 올바르지 않은 경우 - 생성 실패")
	void validateEmail_NotValidFormat_ExceptionThrown(String invalidEmail) {
		assertThrows(IllegalArgumentException.class,
			() -> createUser(validLoginId, validPassword, validNickname, validBirthDate, validPhone, invalidEmail,
				validAuthority));
	}

	@Test
	@DisplayName("validateAuthroity - 유저의 Authority 값이 null인 경우 - 생성 실패")
	void validateAuthority_EmptyValue_ExceptionThrown() {
		Authority invalidAuthority = null;

		assertThrows(NullPointerException.class,
			() -> createUser(validLoginId, validPassword, validNickname, validBirthDate, validPhone, validEmail,
				invalidAuthority));
	}
}