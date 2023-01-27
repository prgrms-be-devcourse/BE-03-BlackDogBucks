package com.prgrms.bdbks.domain.coupon.repository;

import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import com.prgrms.bdbks.CustomDataJpaTest;
import com.prgrms.bdbks.config.jpa.JpaConfig;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.testutil.CouponObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@CustomDataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Import(JpaConfig.class)
class CouponRepositoryTest {

	private final CouponRepository couponRepository;

	private final UserRepository userRepository;

	private User user;

	@BeforeEach
	void setup() {
		user = createUser();
		userRepository.save(user);
	}

	@DisplayName("findAllByUserIdAndUsed - 사용자의 미사용 쿠폰들을 조회할 수 있다. - 성공")
	@Test
	void findAllByUserIdAndUsed_validParameters_Success() {

		//given
		List<Coupon> coupons = CouponObjectProvider.createCoupon(user.getId());
		couponRepository.saveAll(coupons);

		//when
		List<Coupon> result = couponRepository.findUnusedCoupon(user.getId());

		//then
		assertThat(result).isNotEmpty();
		assertThat(result).hasSize(coupons.size());

		result.forEach(coupon -> assertFalse(coupon.isUsed()));
	}

}