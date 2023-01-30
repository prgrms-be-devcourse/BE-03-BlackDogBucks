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

	@NotBlank(message = "카테고리 이름이 입력되지 않았습니다.")
	@Max(value = 30, message = "이름은 최대 30자 까지 허용합니다.")
	private String name;

	@NotBlank(message = "카테고리 영어 이름이 입력되지 않았습니다.")
	@Max(value = 30, message = "영어 이름은 최대 30자 까지 허용합니다.")
	private String englishName;

	@NotNull(message = "아이템 타입이 입력되지 않았습니다.")
	private ItemType itemType;

}
