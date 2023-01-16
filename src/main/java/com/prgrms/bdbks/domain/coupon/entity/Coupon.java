package com.prgrms.bdbks.domain.coupon.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(length = 10)
	private String name;

	private int price;

	private LocalDateTime expireDate;

	@Column(nullable = false)
	private boolean isUsed;

	@Builder
	protected Coupon(Long userId, String name, int price, LocalDateTime expireDate, boolean isUsed) {
		this.userId = userId;
		this.name = name;
		this.price = price;
		this.expireDate = expireDate;
		this.isUsed = isUsed;
	}
}
