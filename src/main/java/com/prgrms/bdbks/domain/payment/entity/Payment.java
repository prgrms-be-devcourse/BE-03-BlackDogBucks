package com.prgrms.bdbks.domain.payment.entity;

import static com.google.common.base.Preconditions.*;
import static com.prgrms.bdbks.domain.payment.PaymentStatus.*;
import static javax.persistence.EnumType.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.payment.PaymentStatus;
import com.prgrms.bdbks.domain.payment.PaymentType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AbstractTimeColumn {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "payment_id")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	private Order order;

	private Long cardId;

	@Enumerated(value = STRING)
	private PaymentType paymentType;

	@Column(nullable = false)
	private int price;

	private LocalDateTime paymentDateTime;

	@Enumerated(value = STRING)
	private PaymentStatus paymentStatus;

	@Builder
	protected Payment(Order order, Long cardId, PaymentType paymentType, int price, LocalDateTime paymentDateTime) {
		validateCardId(cardId);
		validatePrice(price);
		validatePaymentType(paymentType);
		validatePaymentDateTime(paymentDateTime);

		this.order = order;
		this.cardId = cardId;
		this.paymentType = paymentType;
		this.price = price;
		this.paymentDateTime = paymentDateTime;
		this.paymentStatus = APPROVE;
	}

	private void validatePaymentType(PaymentType paymentType) {
		checkNotNull(paymentType, "결제 타입을 입력해주세요");
	}

	private void validatePrice(int price) {
		checkArgument(price >= 0, "결제 금액은 0원부터 가능합니다.");
	}

	private void validateCardId(Long cardId) {
		checkNotNull(cardId, "카드 아이디를 입력해주세요");
	}

	private void validatePaymentDateTime(LocalDateTime paymentDateTime) {
		checkNotNull(paymentDateTime, "결제 시간을 입력해주세요");
	}

	//TODO 주문 금액과 충전 카드의 금액을 비교해야 함(결제 가능한 경우 충전카드의 메소드 호출)
	//TODO 충전 카드에 충전 요청 시 충전 금액을 검증해야 함(충전 가능한 경우 충전카드의 메소드 호출)
}
