package com.prgrms.bdbks.domain.card.entity;

import static com.google.common.base.Preconditions.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cards")
@NoArgsConstructor(access = PROTECTED)
public class Card extends AbstractTimeColumn {
	public static final int MIN_CHARGE_PRICE = 10000;
	public static final int MAX_CHARGE_PRICE = 550000;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "card_id")
	private String id;

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	@Column(nullable = false)
	private int amount;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	protected Card( User user, String name) {
		validateUser(user);

		this.user = user;
		this.name = name;
	}

	private void validateAmount(int amount) {
		checkArgument(amount >= 0, "거래는 0원 이상부터 가능합니다.");
	}

	private void validateUser(User user) {
		checkNotNull(user, "user를 입력해주세요.");
	}

	private void compareAmount(int amount) {
		checkArgument(this.amount >= amount, "결제금액은 충전금액보다 클 수 없습니다.");
	}

	private void validateChargeAmount(int amount) {
		checkArgument(MIN_CHARGE_PRICE <= amount && amount <= MAX_CHARGE_PRICE, "충전 금액은 10,000~550,000원 까지 가능합니다.");
	}

	public void chargeAmount(int amount) {
		validateChargeAmount(amount);
		this.amount += amount;
	}

	public void payAmount(int amount) {
		compareAmount(amount);
		validateAmount(amount);
		this.amount -= amount;
	}

	public void compareUser(Long userId) {
		checkArgument(Objects.equals(this.user.getId(), userId));
	}
	
}