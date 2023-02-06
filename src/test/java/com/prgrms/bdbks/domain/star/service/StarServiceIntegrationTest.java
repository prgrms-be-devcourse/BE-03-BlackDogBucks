package com.prgrms.bdbks.domain.star.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.star.repository.StarRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StarServiceIntegrationTest {

	@MockBean
	private final StoreService storeService;

	private final UserRepository userRepository;

	private final StarRepository starRepository;

	private final StarService starService;

	private final User user = UserObjectProvider.createUser();

	@BeforeEach
	void setup() {
		userRepository.save(user);
	}

	@DisplayName("create - 사용자의 별을 만들 수 있다. - 성공")
	@Test
	void create_ValidUser_CreateSuccess() {
		//given
		int expectSize = 1;

		//when
		starService.create(user);

		//then
		assertEquals(starRepository.findAll().size(), expectSize);
	}

	@DisplayName("findById - 미등록 사용자의 별은 조회할 수 없다.")
	@Test
	void findById_InvalidUser_ExceptionThrown() {
		//given
		starService.create(user);
		Long unknownUserId = 100L;

		//when & then
		assertThrows(EntityNotFoundException.class, () -> starService.findByUserId(unknownUserId));
	}

	@DisplayName("findById - 사용자의 별을 조회할 수 있다. - 성공")
	@Test
	void findById_validUser_findSuccess() {
		//given
		Long userId = user.getId();
		starService.create(user);

		//when & then
		assertDoesNotThrow(() -> starService.findByUserId(userId));
	}

	@DisplayName("delete - 사용자의 별을 삭제할 수 있다. - 성공")
	@Test
	void delete_ValidUserId_DeleteSuccess() {
		//given
		Long userId = user.getId();

		//when
		starService.delete(userId);

		//then
		assertFalse(starRepository.findByUserId(userId).isPresent());
	}

}