package com.prgrms.bdbks.domain.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import com.prgrms.bdbks.CustomDataJpaTest;

import lombok.RequiredArgsConstructor;

@CustomDataJpaTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserRepositoryTest {

	private final UserRepository userRepository;

	@Test
	void queryTest() {
		userRepository.findByLoginIdWithAuthorities("loginId");
	}
}