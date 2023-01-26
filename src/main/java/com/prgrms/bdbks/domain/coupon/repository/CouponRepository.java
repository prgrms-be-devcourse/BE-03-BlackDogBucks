package com.prgrms.bdbks.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.coupon.entity.Coupon;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	List<Coupon> findByUserId(Long userId);
}