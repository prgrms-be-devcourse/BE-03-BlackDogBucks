package com.prgrms.bdbks.domain.card.api;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.bdbks.domain.card.dto.CardSaveRequest;
import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.user.entity.User;

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
	 * @param user - 등록할 충전카드의 User
	 * @param cardSaveRequest - 등록할 충전카드의 User
	 * @return status : created , body : CardSaveResponse
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CardSaveResponse> create(@RequestBody CardSaveRequest cardSaveRequest,
		@SessionAttribute("user") User user) {
		CardSaveResponse cardSaveResponse = cardService.create(user.getId(), cardSaveRequest);

		String createdURI = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/"
			+ cardSaveResponse.getChargeCardId();

		return ResponseEntity.created(URI.create(createdURI)).build();
	}

	/**
	 * <pre>
	 *     충전 카드 단건 조회
	 * </pre>
	 * @param user - 조회할 충전카드의 User
	 * @param cardId - 조회할 충전카드의 Id
	 * @return status : ok, body : CardSearchResponse
	 */
	@GetMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CardSearchResponse> getCard(@SessionAttribute("user") User user,
		@PathVariable String cardId) {
		CardSearchResponse cardSearchResponse = cardService.findByCardId(cardId);

		return ResponseEntity.ok(cardSearchResponse);
	}

}