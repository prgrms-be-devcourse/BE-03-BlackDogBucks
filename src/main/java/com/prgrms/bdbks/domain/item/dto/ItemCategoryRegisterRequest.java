package com.prgrms.bdbks.domain.item.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemCategoryRegisterRequest {

	@NotBlank
	@Max(30)
	private String name;

	@NotBlank
	@Max(30)
	private String englishName;

	@NotNull
	private ItemType itemType;

}
