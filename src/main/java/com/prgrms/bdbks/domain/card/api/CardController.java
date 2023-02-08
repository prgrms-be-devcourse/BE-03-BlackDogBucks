package com.prgrms.bdbks.domain.card.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.bdbks.domain.card.dto.CardChargeRequest;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardSaveRequest;
import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.jwt.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

	private final CardService cardService;

	/**
	 * <pre>
	 *     충전 카드 등록
	 * </pre>
	 *
	 * @param userDetails     : 유저 정보
	 * @param cardSaveRequest : 카드 정보
	 * @return status : created , body : CardSaveResponse
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CardSaveResponse> create(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid CardSaveRequest cardSaveRequest) {
		User user = userDetails.getUser();
		CardSaveResponse cardSaveResponse = cardService.create(user.getId(), cardSaveRequest);

		String createdURI = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/"
			+ cardSaveResponse.getChargeCardId();

		return ResponseEntity.created(URI.create(createdURI)).build();
	}

	/**
	 * <pre>
	 *     충전 카드 단건 조회
	 * </pre>
	 *
	 * @param cardId - 조회할 충전카드의 Id
	 * @return status : ok, body : CardSearchResponse
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@GetMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CardSearchResponse> getCard(@PathVariable String cardId) {
		CardSearchResponse cardSearchResponse = cardService.findByCardId(cardId);

		return ResponseEntity.ok(cardSearchResponse);
	}

	/**
	 * <pre>
	 * 충전 카드 금액 충전
	 * </pre>
	 *
	 * @param cardChargeRequest - 충전할 카드정보
	 * @return status : ok, body : CardChargeResponse
	 */
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STORE_MANAGER')")
	@PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CardChargeResponse> chargeCard(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid CardChargeRequest cardChargeRequest) {
		CardChargeResponse cardChargeResponse = cardService.charge(userDetails.getUser().getId(),
			cardChargeRequest.getChargeCardId(),
			cardChargeRequest.getAmount());

		return ResponseEntity.ok(cardChargeResponse);
	}
}