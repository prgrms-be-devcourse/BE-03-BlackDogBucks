package com.prgrms.bdbks.domain.coupon.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponses;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.CustomUserDetails;

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
	 *
	 * @param userDetails : 유저 정보
	 * @param used        : 쿠폰 사용 여부
	 * @return status : ok , body : CouponSearchResponses
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CouponSearchResponses> findUnusedCoupon(
		@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "false") boolean used) {
		User user = userDetails.getUser();
		CouponSearchResponses searchResponses = couponService.findUnusedCoupon(user.getId(), used);
		return ResponseEntity.ok(searchResponses);
	}

	/**
	 * <pre>
	 *     사용자의 모든 Coupon 조회
	 * </pre>
	 *
	 * @param userDetails : 유저 정보
	 * @return status : ok , body : CouponSearchResponses
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CouponSearchResponses> findAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();
		CouponSearchResponses searchResponses = couponService.findAllByUserId(user.getId());
		return ResponseEntity.ok(searchResponses);
	}

}
