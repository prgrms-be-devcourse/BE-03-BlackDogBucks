package com.prgrms.bdbks.domain.payment.service;

import static com.prgrms.bdbks.domain.testutil.CardObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.common.exception.PaymentException;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.repository.OrderRepository;
import com.prgrms.bdbks.domain.payment.dto.PaymentRefundResult;
import com.prgrms.bdbks.domain.payment.entity.Payment;
import com.prgrms.bdbks.domain.payment.entity.PaymentStatus;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;
import com.prgrms.bdbks.domain.payment.repository.PaymentRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class DefaultPaymentServiceIntegrationTest {

	private final PaymentService paymentService;

	private final PaymentRepository paymentRepository;

	private final CardRepository cardRepository;

	private final UserRepository userRepository;

	private final OrderRepository orderRepository;

	private final User user = UserObjectProvider.createUser();

	private Card card;

	private final Order order = OrderObjectProvider.createOrder();

	@MockBean
	private final StoreService storeService;

	@BeforeEach
	void setUp() {
		userRepository.save(user);
		orderRepository.save(order);
		card = cardRepository.save(createCard(user));
	}

	@ParameterizedTest
	@ValueSource(ints = {10000, 20000, 50000, 500000, 549999, 550000})
	@DisplayName("chargePay - ???????????? ??????????????? ????????? ????????? ??? ??????. - ??????")
	void chargePay_ValidPrice_Success(int totalPrice) {
		//when
		PaymentResult paymentResult = paymentService.chargePay(card.getId(), totalPrice);

		//then
		Optional<Payment> optionalPayment = paymentRepository.findById(paymentResult.getPaymentId());
		assertTrue(optionalPayment.isPresent());

		Payment savedPayment = optionalPayment.get();

		assertThat(savedPayment)
			.hasFieldOrPropertyWithValue("id", paymentResult.getPaymentId())
			.hasFieldOrPropertyWithValue("chargeCardId", card.getId())
			.hasFieldOrPropertyWithValue("paymentStatus", PaymentStatus.APPROVE)
			.hasFieldOrPropertyWithValue("paymentType", PaymentType.CHARGE);
	}

	@ParameterizedTest
	@ValueSource(ints = {-50000, -500, 100, 200, 999, 550001, 10000000})
	@DisplayName("chargePay - ???????????? ??????????????? ????????? ???????????? ????????? ????????? ??? ??????. - ??????")
	void chargePay_InvalidPrice_Success(int totalPrice) {
		assertThrows(PaymentException.class, () -> paymentService.chargePay(card.getId(), totalPrice));
	}

	@DisplayName("orderPayRefund - ???????????? ?????? ????????? ???????????? ??????????????? ????????? ????????????. - ??????")
	@Test
	void orderPayRefund_validOrder_Success() {
		int orderPrice = 5000;

		Payment payment = Payment.createOrderPayment(order, card.getId(), orderPrice);
		paymentRepository.save(payment);
		PaymentRefundResult paymentRefundResult = paymentService.orderPayRefund(payment.getOrder().getId());

		Optional<Payment> optionalPayment = paymentRepository.findById(payment.getId());
		assertTrue(optionalPayment.isPresent());

		Payment savedPayment = optionalPayment.get();

		assertThat(paymentRefundResult)
			.hasFieldOrPropertyWithValue("id", savedPayment.getId())
			.hasFieldOrPropertyWithValue("chargeCardId", savedPayment.getChargeCardId())
			.hasFieldOrPropertyWithValue("price", savedPayment.getPrice());
	}

	@DisplayName("orderPayRefund - ???????????? ?????? ????????? ????????? ???????????????. - ??????")
	@Test
	void orderPayRefund_inValidOrder_Fail() {
		int orderPrice = 5000;
		String inValidId = "wrongId";
		Payment payment = Payment.createOrderPayment(order, card.getId(), orderPrice);
		paymentRepository.save(payment);

		assertThrows(EntityNotFoundException.class, () -> paymentService.orderPayRefund(inValidId));
	}
}
