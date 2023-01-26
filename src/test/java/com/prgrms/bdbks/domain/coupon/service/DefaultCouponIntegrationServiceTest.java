package com.prgrms.bdbks.domain.coupon.service;

import java.util.List;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponses;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class DefaultCouponIntegrationServiceTest {
	private final CouponService couponService;

	private final UserRepository userRepository;

	private final User user = UserObjectProvider.createUser();

	@BeforeEach
	void setUp() {
		userRepository.save(user);
	}

	@Test
	@DisplayName("getCouponList - 쿠폰을 만들어 사용자를 할당한다. - 성공")
	void create_validUser_Success() {
		CouponSaveResponse response = couponService.create(user.getId());

		Assertions.assertThat(response)
			.hasFieldOrPropertyWithValue("userId", user.getId())
			.hasFieldOrPropertyWithValue("name", "설맞이 쿠폰")
			.hasFieldOrPropertyWithValue("price", 6000)
			.hasFieldOrPropertyWithValue("expireDate", response.getExpireDate());
	}

	@DisplayName("getCouponList - 사용자의 쿠폰 목록을 조회할 수 있다. - 성공")
	@Test
	void getCouponList_validUser_Success() {
		couponService.create(user.getId());

		CouponSearchResponses couponList = couponService.findAllByUserId(user.getId());
		List<CouponSearchResponse> couponListDto = couponList.getCouponSearchResponses();

		Assertions.assertThat(couponListDto).hasSize(1);

		Assertions.assertThat(couponListDto.get(0))
			.hasFieldOrPropertyWithValue("userId", user.getId())
			.hasFieldOrPropertyWithValue("name", "설맞이 쿠폰")
			.hasFieldOrPropertyWithValue("price", 6000)
			.hasFieldOrPropertyWithValue("expireDate", couponListDto.get(0).getExpireDate());
	}
}
