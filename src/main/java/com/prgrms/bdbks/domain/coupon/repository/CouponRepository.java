package com.prgrms.bdbks.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	List<Coupon> findByUserId(Long userId);

	@Query("select c from Coupon c where c.userId = :userId and c.used = false")
	List<Coupon> findUnusedCoupon(Long userId);
}