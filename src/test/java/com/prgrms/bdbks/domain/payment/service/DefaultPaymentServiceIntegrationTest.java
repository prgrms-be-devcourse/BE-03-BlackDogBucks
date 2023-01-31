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

	//TODO Order Repository 만들어지먼 orderPay 테스트 작성
	//Payment 결제상태가 CHARGE인지, 충전카드 금액 검증,
	@ParameterizedTest
	@ValueSource(ints = {10000, 20000, 50000, 500000, 549999, 550000})
	@DisplayName("chargePay - 사용자의 충전카드에 금액을 충전할 수 있다. - 성공")
	void chargePay_ValidPrice_Success(int totalPrice) {
		//when
		PaymentResult paymentResult = paymentService.chargePay(card.getChargeCardId(), totalPrice);

		//then
		Optional<Payment> optionalPayment = paymentRepository.findById(paymentResult.getPaymentId());
		assertTrue(optionalPayment.isPresent());

		Payment savedPayment = optionalPayment.get();

		assertThat(savedPayment)
			.hasFieldOrPropertyWithValue("id", paymentResult.getPaymentId())
			.hasFieldOrPropertyWithValue("chargeCardId", card.getChargeCardId())
			.hasFieldOrPropertyWithValue("paymentStatus", PaymentStatus.APPROVE)
			.hasFieldOrPropertyWithValue("paymentType", PaymentType.CHARGE);
	}

	@ParameterizedTest
	@ValueSource(ints = {-50000, -500, 100, 200, 999, 550001, 10000000})
	@DisplayName("chargePay - 사용자의 충전카드에 한도를 벗어나는 금액은 충전할 수 없다. - 실패")
	void chargePay_InvalidPrice_Success(int totalPrice) {

		assertThrows(PaymentException.class, () -> paymentService.chargePay(card.getChargeCardId(), totalPrice));
	}

	@DisplayName("orderPayRefund - 주문으로 결제 내역을 조회하여 결제상태를 환불로 변경한다. - 성공")
	@Test
	void orderPayRefund_validOrder_Success() {

		int orderPrice = 5000;

		Payment payment = Payment.createOrderPayment(order, card.getChargeCardId(), orderPrice);
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

	@DisplayName("orderPayRefund - 유효하지 않는 주문은 환불이 불가능하다. - 실패")
	@Test
	void orderPayRefund_inValidOrder_Fail() {

		int orderPrice = 5000;
		String inValidId = "wrongId";
		Payment payment = Payment.createOrderPayment(order, card.getChargeCardId(), orderPrice);
		paymentRepository.save(payment);

		assertThrows(EntityNotFoundException.class, () -> paymentService.orderPayRefund(inValidId));

	}
}
