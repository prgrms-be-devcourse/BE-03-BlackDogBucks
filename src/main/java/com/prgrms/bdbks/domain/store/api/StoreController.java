package com.prgrms.bdbks.domain.store.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prgrms.bdbks.domain.store.dto.StoreCreateRequest;
import com.prgrms.bdbks.domain.store.dto.StoreResponse;
import com.prgrms.bdbks.domain.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<URI> createStore(@RequestBody StoreCreateRequest storeCreateRequest) {
		String currentURI = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
		String createdURI = String.format("%s/%s", currentURI, storeService.createStore(storeCreateRequest));
		return ResponseEntity.created(URI.create(createdURI)).build();
	}

	@GetMapping(value = "/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StoreResponse.StoreInformation> findStoreById(@PathVariable String storeId) {
		StoreResponse.StoreInformation storeResponse = storeService.findStoreById(storeId);
		return ResponseEntity.ok(storeResponse);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StoreResponse.StoreInformation>> findStoresByDistrict(
		@RequestParam String district) {
		List<StoreResponse.StoreInformation> stores = storeService.findAllByDisStrictName(district);
		return ResponseEntity.ok(stores);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StoreResponse.StoreInformation>> findStoresByLocation(
		@RequestParam long latitude, @RequestParam long longitude) {
		List<StoreResponse.StoreInformation> stores = storeService.findAllByPoint(latitude, longitude);
		return ResponseEntity.ok(stores);
	}

}