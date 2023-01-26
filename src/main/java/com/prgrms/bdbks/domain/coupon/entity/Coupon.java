package com.prgrms.bdbks.domain.coupon.entity;

import static com.google.common.base.Preconditions.*;
import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false, length = 10)
	private String name;

	private int price;

	@Column(nullable = false)
	private LocalDateTime expireDate;

	private boolean used;

	@Builder
	protected Coupon(Long userId, String name, int price, LocalDateTime expireDate) {
		validateUserId(userId);
		validateName(name);
		validatePrice(price);
		validateExpireDate(expireDate);

		this.userId = userId;
		this.name = name;
		this.price = price;
		this.expireDate = expireDate;
		this.used = false;
	}

	public static Coupon createCoupon(Long userId) {
		return Coupon.builder()
			.userId(userId)
			.name("설맞이 쿠폰")
			.price(6000)
			.expireDate(LocalDateTime.now().plusMonths(6L))
			.build();
	}

	private void validateUserId(Long userId) {
		checkNotNull(userId, "유저 아이디를 입력해주세요.");
	}

	private void validateName(String name) {
		checkArgument(StringUtils.hasText(name), "올바른 쿠폰명을 입력해주세요.");
		checkArgument(name.length() <= 10, "이름은 10자 이하로 작성해주세요.");
	}

	private void validatePrice(int price) {
		checkArgument(price >= 0, "쿠폰 금액은 0원부터 가능합니다.");
	}

	private void validateExpireDate(LocalDateTime expireDate) {
		checkArgument(expireDate.isAfter(now()), "만료일은 현재시간보다 전일 수 없습니다.");
		checkNotNull(expireDate, "만료일을 입력해주세요.");
	}

	public void useCoupon() {
		this.used = true;
	}
}