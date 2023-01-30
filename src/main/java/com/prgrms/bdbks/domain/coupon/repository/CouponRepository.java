package com.prgrms.bdbks.domain.coupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.bdbks.domain.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	List<Coupon> findByUserId(Long userId);

	@Query("select c from Coupon c where c.userId = :userId and c.used = :used")
	List<Coupon> findUnusedCoupon(@Param("userId") Long userId, @Param("used") boolean used);
}