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
import org.springframework.util.StringUtils;

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
	@Column(name = "charge_card_id")
	@GenericGenerator(name = "card_id_generator",
		strategy = "com.prgrms.bdbks.domain.card.repository.CardIdGenerator")
	@GeneratedValue(generator = "card_id_generator")
	private String chargeCardId;

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	@Column(nullable = false)
	private int amount;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	protected Card(User user, String name) {
		validateUser(user);
		validateName(name);

		this.user = user;
		this.name = name;
	}

	private void validatePrice(int amount) {
		checkArgument(amount >= 0, "금액은 0원 이상부터 가능합니다.");
	}

	private void validateUser(User user) {
		checkNotNull(user, "user를 입력해주세요.");
	}

	private void validateName(String name) {
		checkArgument(StringUtils.hasText(name), "카드명을 입력해주세요.");
	}

	private void comparePrice(int amount) {
		checkArgument(this.amount >= amount, "결제금액은 충전금액보다 클 수 없습니다.");
	}

	private void validateChargeAmount(int amount) {
		checkArgument(MIN_CHARGE_PRICE <= amount && amount <= MAX_CHARGE_PRICE, "충전 금액은 10,000~550,000원 까지 가능합니다.");
	}

	public void chargeAmount(int amount) {
		validateChargeAmount(amount);
		this.amount += amount;
	}

	public void payPrice(int price) {
		comparePrice(price);
		validatePrice(price);
		this.amount -= price;
	}

	public void refundPrice(int price) {
		validatePrice(price);
		this.amount += price;
	}

	public void compareUser(Long userId) {
		checkArgument(Objects.equals(this.user.getId(), userId));
	}

	public static Card createCard(User user, String name) {

		return Card.builder()
			.user(user)
			.name(name)
			.build();
	}

}