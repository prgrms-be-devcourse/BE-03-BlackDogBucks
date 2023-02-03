package com.prgrms.bdbks.domain.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeRequest;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentFacadeService {

	private final PaymentService paymentService;

	private final CardService cardService;

	@Transactional
	public PaymentResult chargePay(Long userId, PaymentChargeRequest paymentChargeRequest) {
		cardService.charge(userId, paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

		return paymentService.chargePay(paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

	}

}
