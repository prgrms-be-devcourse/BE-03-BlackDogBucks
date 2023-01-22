package com.prgrms.bdbks.domain.item.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponses;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.service.ItemFacadeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/items")
@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemFacadeService itemService;

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<URI> createItem(@RequestBody @Valid ItemCreateRequest itemCreateRequest) {
		Long itemId = itemService.createItem(itemCreateRequest);

		String createdURI = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/" + itemId;

		return ResponseEntity.created(URI.create(createdURI)).build();
	}

	@GetMapping(value = "/{itemId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemDetailResponse> findItemDetail(@PathVariable Long itemId) {
		ItemDetailResponse itemDetail = itemService.findItemDetail(itemId);
		return ResponseEntity.ok(itemDetail);
	}

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemResponses> findItemsByCategory(@RequestParam ItemType kinds,
		@RequestParam String category) {
		ItemResponses items = itemService.findByCategoryName(kinds, category);
		return ResponseEntity.ok(items);
	}

}
