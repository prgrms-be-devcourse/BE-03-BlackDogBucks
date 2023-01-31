package com.prgrms.bdbks.domain.coupon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.coupon.converter.CouponMapper;
import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponses;
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
	public CouponSaveResponses createByStar(Long userId, int couponCount) {

		List<CouponSaveResponse> couponSaveResponses = new ArrayList<>();
		while (couponCount-- > 0) {
			couponSaveResponses.add(create(userId));
		}

		return new CouponSaveResponses(couponSaveResponses);

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

}