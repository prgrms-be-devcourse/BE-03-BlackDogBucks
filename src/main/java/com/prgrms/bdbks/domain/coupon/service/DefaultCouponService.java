package com.prgrms.bdbks.domain.coupon.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.coupon.converter.CouponMapper;
import com.prgrms.bdbks.domain.coupon.dto.CouponRefundResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponses;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCouponService implements CouponService {

	private final CouponMapper couponMapper;

	private final CouponRepository couponRepository;

	@Transactional
	@Override
	public CouponSaveResponse create(Long userId) {

		Coupon coupon = Coupon.createCoupon(userId);

		Coupon saveCoupon = couponRepository.save(coupon);

		return couponMapper.toCouponSaveResponse(saveCoupon);
	}

	@Transactional
	@Override
	public void createByStar(Long userId, boolean canExchange) {
		if (canExchange) {
			create(userId);
		}
	}

	@Override
	public CouponSearchResponses findAllByUserId(Long userId) {
		List<Coupon> coupons = couponRepository.findByUserId(userId);

		return new CouponSearchResponses(
			coupons.stream()
				.map(couponMapper::toCouponSearchResponse)
				.collect(Collectors.toList()));
	}

	@Override
	public Coupon getCouponByCouponId(Long couponId) {
		return couponRepository.findById(couponId)
			.orElseThrow(() -> new EntityNotFoundException(Coupon.class, couponId));
	}

	@Override
	public CouponSearchResponses findUnusedCoupon(Long userId, boolean used) {
		List<Coupon> coupons = couponRepository.findUnusedCoupon(userId, used);

		return new CouponSearchResponses(
			coupons.stream()
				.map(couponMapper::toCouponSearchResponse)
				.collect(Collectors.toList()));
	}

	@Override
	@Transactional
	public CouponRefundResponse refundCoupon(Long couponId) {
		Coupon coupon = getCouponByCouponId(couponId);
		coupon.refund();

		return new CouponRefundResponse(couponId);

	}

}