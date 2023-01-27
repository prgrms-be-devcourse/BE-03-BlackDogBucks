package com.prgrms.bdbks.domain.order.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.service.OrderFacadeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderFacadeService orderService;

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest) {
		orderService.createOrder(orderCreateRequest);
		// 가칭
		String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();

		return ResponseEntity.created(URI.create(currentUri)).build();
	}

	@GetMapping(value = "/{orderId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderDetailResponse> findOrderById(@PathVariable String orderId) {
		return ResponseEntity.ok().body(orderService.findOrderById(orderId));
	}

}
