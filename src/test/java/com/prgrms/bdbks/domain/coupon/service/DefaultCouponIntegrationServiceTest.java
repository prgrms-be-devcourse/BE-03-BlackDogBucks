package com.prgrms.bdbks.domain.coupon.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponses;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.repository.CouponRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.CouponObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class DefaultCouponIntegrationServiceTest {

	@MockBean
	private final StoreService storeService;

	private final CouponService couponService;

	private final CouponRepository couponRepository;

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

		assertThat(response)
			.hasFieldOrPropertyWithValue("userId", user.getId())
			.hasFieldOrPropertyWithValue("name", "설맞이 쿠폰")
			.hasFieldOrPropertyWithValue("price", 6000)
			.hasFieldOrPropertyWithValue("expireDate", response.getExpireDate());
	}

	@DisplayName("getCouponList - 사용자의 모든 쿠폰 목록을 조회할 수 있다. - 성공")
	@Test
	void getCouponList_validUser_Success() {
		couponService.create(user.getId());

		CouponSearchResponses couponList = couponService.findAllByUserId(user.getId());
		List<CouponSearchResponse> couponSearchResponses = couponList.getCouponSearchResponses();

		assertThat(couponSearchResponses).hasSize(1);

		assertThat(couponSearchResponses.get(0))
			.hasFieldOrPropertyWithValue("userId", user.getId())
			.hasFieldOrPropertyWithValue("name", "설맞이 쿠폰")
			.hasFieldOrPropertyWithValue("price", 6000)
			.hasFieldOrPropertyWithValue("expireDate", couponSearchResponses.get(0).getExpireDate());
	}

	@DisplayName("findUnusedCoupon - 사용하지 않은 사용자의 쿠폰 목록을 조회할 수 있다. - 성공")
	@Test
	void findUnusedCoupon_validUser_Success() {
		List<Coupon> coupons = CouponObjectProvider.createCoupon(user.getId());
		couponRepository.saveAll(coupons);
		CouponSearchResponses unusedCoupons = couponService.findUnusedCoupon(user.getId(), false);

		List<CouponSearchResponse> couponResponses = unusedCoupons.getCouponSearchResponses();

		assertThat(couponResponses).hasSize(coupons.size());
		assertThat(couponResponses)
			.extracting("used")
			.contains(false);
	}

}
