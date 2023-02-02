package com.prgrms.bdbks.domain.order.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.bdbks.common.dto.SliceResponse;
import com.prgrms.bdbks.domain.order.dto.OrderAcceptRequest;
import com.prgrms.bdbks.domain.order.dto.OrderByStoreResponse;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.dto.OrderCreateResponse;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.dto.OrderRejectRequest;
import com.prgrms.bdbks.domain.order.dto.OrderSearchRequest;
import com.prgrms.bdbks.domain.order.service.OrderFacadeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderFacadeService orderService;

	/**
	 * <pre>
	 *     주문 생성
	 * </pre>
	 *
	 * @param orderCreateRequest
	 * @return status : created, body : 생성된 주문 단건 조회 redirectUri
	 */
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest) {
		OrderCreateResponse response = orderService.createOrder(orderCreateRequest);

		String redirectUri =
			ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/" + response.getOrderId();

		return ResponseEntity.created(URI.create(redirectUri)).body(response);
	}

	/**
	 * <pre>
	 *     주문 단건 조회
	 * </pre>
	 *
	 * @param orderId - 조회할 주문 id
	 * @return status : ok, body : OrderDetailResponse
	 */
	@GetMapping(value = "/{orderId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderDetailResponse> findOrderById(@PathVariable String orderId) {
		return ResponseEntity.ok().body(orderService.findOrderById(orderId));
	}

	/**
	 * <pre>
	 *     주문 승인
	 * </pre>
	 *
	 * @param orderId - 주문 id
	 * @param request - 관리자 id
	 * @return status : ok
	 */
	@PatchMapping(value = "/{orderId}/accept",
		consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> acceptOrder(@PathVariable String orderId,
		@RequestBody @Valid OrderAcceptRequest request) {

		orderService.acceptOrder(orderId, request.getUserId());

		return ResponseEntity.ok().build();
	}

	/**
	 * <pre>
	 *     주문 거절
	 * </pre>
	 *
	 * @param orderId - 주문 id
	 * @param request - 관리자 id
	 * @return status : ok
	 */
	@PatchMapping(value = "/{orderId}/reject",
		consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> rejectOrder(@PathVariable String orderId,
		@RequestBody @Valid OrderRejectRequest request) {
		orderService.rejectOrder(orderId, request.getUserId());

		return ResponseEntity.ok().build();
	}

	/**
	 * <pre>
	 *     매장 주문 리스트 조회
	 * </pre>
	 *
	 * @param request - 조회할 매장 id와 태주문 상태
	 * @return SliceResponse<StoreOrderDto> - 커서기반 order list
	 */
	@GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SliceResponse<OrderByStoreResponse>> findStoreOrders(
		@ModelAttribute OrderSearchRequest request) {

		SliceResponse<OrderByStoreResponse> response = orderService.findAllStoreOrdersBy(request.getUserId(),
			request.getOrderStatus(), request.getCursorOrderId(),
			PageRequest.of(0, request.getPageSize(), Sort.by(request.getDirection(), request.getBy().getFieldName())));

		return ResponseEntity.ok(response);
	}

}
