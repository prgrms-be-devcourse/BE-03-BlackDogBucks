package com.prgrms.bdbks.domain.coupon.converter;

import org.mapstruct.Mapper;

import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponse;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;

@Mapper(componentModel = "spring")
public interface CouponMapper {

	CouponSearchResponse toCouponSearchResponse(Coupon coupon);

	CouponSaveResponse toCouponSaveResponse(Coupon coupon);
}