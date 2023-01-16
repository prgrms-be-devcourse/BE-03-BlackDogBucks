package com.prgrms.bdbks.domain.payment.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.payment.PaymentStatus;
import com.prgrms.bdbks.domain.payment.PaymentType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	private Order order;

	private Long cardId;

	@Enumerated(value = EnumType.STRING)
	private PaymentType paymentType;

	@Column(nullable = false)
	private int price;

	private LocalDateTime paymentDateTime;

	@Enumerated(value = EnumType.STRING)
	private PaymentStatus paymentStatus;

	@Builder
	protected Payment(Order order, Long cardId, PaymentType paymentType, int price, LocalDateTime paymentDateTime,
		PaymentStatus paymentStatus) {
		this.order = order;
		this.cardId = cardId;
		this.paymentType = paymentType;
		this.price = price;
		this.paymentDateTime = paymentDateTime;
		this.paymentStatus = paymentStatus;
	}
}
