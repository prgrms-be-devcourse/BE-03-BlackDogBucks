package com.prgrms.bdbks.domain.coupon.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponse;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;

@Mapper(componentModel = "spring")
public interface CouponMapper {

	@Mappings({
		@Mapping(source = "id", target = "couponId"),
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "price", target = "price"),
		@Mapping(source = "expireDate", target = "expireDate"),
		@Mapping(source = "used", target = "used")
	})
	CouponSearchResponse toCouponSearchResponse(Coupon coupon);

	@Mappings({
		@Mapping(source = "id", target = "couponId"),
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "price", target = "price"),
		@Mapping(source = "expireDate", target = "expireDate")
	})
	CouponSaveResponse toCouponSaveResponse(Coupon coupon);
}