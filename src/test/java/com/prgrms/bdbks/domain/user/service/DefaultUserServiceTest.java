package com.prgrms.bdbks.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.DuplicateInsertException;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserAuthChangeRequest;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.role.Role;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Sql({"/test_sql/user.sql"})
class DefaultUserServiceTest {

	private final UserService userService;

	@Test
	@DisplayName("register - 회원가입에 성공하고 결과값으로 User를 반환하는데 성공한다.")
	void register_success() {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();

		User result = userService.register(userCreateRequest);

		assertNotNull(result);
		assertEquals(userCreateRequest.getLoginId(), result.getLoginId());
		assertNotEquals(userCreateRequest.getPassword(), result.getPassword());
	}

	@Test
	@DisplayName("register - 중복된 가입으로 회원가입에 실패한다.")
	void register_failure() {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();
		userService.register(userCreateRequest);
		assertThrows(DuplicateInsertException.class, () -> userService.register(userCreateRequest));
	}

	@Test
	@DisplayName("login - 존재하는 유저의 로그인에 성공한다.")
	void login_success() {
		userService.register(UserObjectProvider.createBlackDogRequest());
		TokenResponse tokenResponse = userService.login(UserObjectProvider.createBlackDogLoginRequest());

		assertNotNull(tokenResponse);
		assertNotNull(tokenResponse.getToken());
	}

	@Test
	@DisplayName("login - 존재하지 않는 유저의 로그인에 실패한다.")
	void login_failure_notExistUser() {
		assertThrows(
			UsernameNotFoundException.class,
			() -> userService.login(new UserLoginRequest("notExistUser", "notExist123")));
	}

	@Test
	@DisplayName("login - 비밀번호가 틀린 경우 로그인이 실패한다.")
	void login_failure_inCorrectPassword() {
		userService.register(UserObjectProvider.createBlackDogRequest());
		assertThrows(BadCredentialsException.class,
			() -> userService.login(new UserLoginRequest(UserObjectProvider.BLACK_DOG_LOGIN_ID, "incorrectPassword")));
	}

	@Test
	@DisplayName("findUser - 존재하는 유저의 로그인 아이디로 유저 정보 조회에 성공한다.")
	void findUser_success() {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();

		User user = userService.register(userCreateRequest);

		Optional<User> findUser = userService.findUser(user.getLoginId());

		assertTrue(findUser.isPresent());
		assertEquals(user, findUser.get());
	}

	@Test
	@DisplayName("findUser - 존재하지 않는 유저의 로그인 아이디로 유저 정보 조회에 실패한다.")
	void findUser_failure() {
		Optional<User> result = userService.findUser("notExistUser");
		assertFalse(result.isPresent());
	}

	@DisplayName("findUserById - PK를 이용하여 저장된 사용자를 조회할 수 있다.")
	@Test
	void findUserById_validId_Success() {
		UserCreateRequest createRequest = UserObjectProvider.createBlackDogRequest();
		User savedUser = userService.register(createRequest);

		User findUser = userService.findUserById(savedUser.getId());

		assertEquals(savedUser, findUser);
	}

	@DisplayName("hasStore - 매장 관리자에게 매장 ID가 없는 경우 예외가 발생한다.")
	@Test
	void hasStore_NoAuthority_Fail() {
		UserCreateRequest createRequest = UserObjectProvider.createBlackDogRequest();
		User savedUser = userService.register(createRequest);
		String storeId = "storeId";

		assertThrows(NullPointerException.class,
			() -> userService.hasStore(savedUser.getId(), storeId));
	}

	@DisplayName("changeUserAuthority - 사용자 정보와 변경할 권한 정보가 유효하면 사용자의 권한 변경에 성공한다.")
	@Test
	void changeUserAuthority_validParameters_Fail() {
		UserCreateRequest createRequest = UserObjectProvider.createBlackDogRequest();
		userService.register(createRequest);

		UserAuthChangeRequest userAuthChangeRequest = new UserAuthChangeRequest(createRequest.getLoginId(),
			Role.ROLE_ADMIN);

		assertDoesNotThrow(() -> userService.changeUserAuthority(userAuthChangeRequest));
	}

	@DisplayName("changeUserAuthority - 사용자 정보가 유효하지 않으면 예외가 발생한다.")
	@Test
	void changeUserAuthority_invalidUser_fail() {
		UserCreateRequest createRequest = UserObjectProvider.createBlackDogRequest();
		userService.register(createRequest);

		String unknownLoginId = "helloWorld";

		UserAuthChangeRequest userAuthChangeRequest = new UserAuthChangeRequest(unknownLoginId,
			Role.ROLE_ADMIN);

		assertThrows(EntityNotFoundException.class,
			() -> userService.changeUserAuthority(userAuthChangeRequest));
	}

}
