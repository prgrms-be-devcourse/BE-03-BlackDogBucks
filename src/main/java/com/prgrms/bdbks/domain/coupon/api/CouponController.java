package com.prgrms.bdbks.domain.coupon.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponses;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/coupons")
@RestController
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;

	/**
	 * <pre>
	 *     사용하지 않은 Coupon 조회
	 * </pre>
	 * @param user - 조회할 Coupon의 User
	 * @return status : ok , body : CouponSearchResponses
	 */
	@GetMapping(value = "/unused", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CouponSearchResponses> findUnusedCoupon(@SessionAttribute("user") User user) {
		CouponSearchResponses searchResponses = couponService.findUnusedCoupon(user.getId());
		return ResponseEntity.ok(searchResponses);
	}

	/**
	 * <pe>
	 *   사용자의 모든 Coupon 조회
	 * </pre>
	 * @param user - 조회할 Coupon의 User
	 * @return status : ok , body : CouponSearchResponses
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CouponSearchResponses> findAll(@SessionAttribute("user") User user) {
		CouponSearchResponses searchResponses = couponService.findAllByUserId(user.getId());
		return ResponseEntity.ok(searchResponses);
	}

}
