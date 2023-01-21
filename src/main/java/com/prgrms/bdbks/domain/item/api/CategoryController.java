package com.prgrms.bdbks.domain.item.api;

import static org.springframework.http.MediaType.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.bdbks.domain.item.dto.ItemCategoriesResponse;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.service.ItemFacadeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final ItemFacadeService itemService;

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findCategoriesByItemType(@RequestParam ItemType kinds) {
		ItemCategoriesResponse categories = itemService.findCategoriesByItemType(kinds);
		return ResponseEntity.ok(categories);
	}
}
