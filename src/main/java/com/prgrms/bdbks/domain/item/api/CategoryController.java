package com.prgrms.bdbks.domain.item.api;

import static org.springframework.http.MediaType.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.bdbks.domain.item.dto.ItemCategoryResponses;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.service.ItemCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final ItemCategoryService itemCategoryService;

	@GetMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemCategoryResponses> findCategoriesByItemType(@RequestParam("kinds") ItemType itemType) {
		ItemCategoryResponses categories = itemCategoryService.findAllByType(itemType);
		return ResponseEntity.ok(categories);
	}

}