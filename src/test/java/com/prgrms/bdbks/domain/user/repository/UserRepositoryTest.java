// package com.prgrms.bdbks.domain.user.repository;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.test.annotation.Rollback;
// import org.springframework.test.context.TestConstructor;
//
// import com.prgrms.bdbks.CustomDataJpaTest;
// import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
// import com.prgrms.bdbks.domain.user.entity.User;
//
// import lombok.RequiredArgsConstructor;
//
// @CustomDataJpaTest
// @RequiredArgsConstructor
// @TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
// class UserRepositoryTest {
//
// 	private final UserRepository userRepository;
//
// 	private User user;
//
// 	@BeforeEach
// 	@Rollback(value = false)
// 	void setup() {
// 		user = UserObjectProvider.createUser();
// 		userRepository.save(user);
// 	}
//
// 	@Test
// 	@DisplayName("조회 - 권힌과 함께 사용자를 조회하는데 성공한다.")
// 	void queryTest() {
// 		Optional<User> optionalUser = userRepository.findByLoginIdWithAuthorities(user.getLoginId());
// 		assertTrue(optionalUser.isPresent());
// 	}
// }