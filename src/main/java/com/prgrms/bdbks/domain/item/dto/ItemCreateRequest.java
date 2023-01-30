package com.prgrms.bdbks.domain.item.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCreateRequest {

	@NotNull(message = "아이템 타입이 선택되지 않았습니다.")
	private ItemType itemType;

	@NotBlank(message = "아이템 카테고리가 입력되지 않았습니다.")
	private String categoryName;

	@NotBlank(message = "아이템 이름이 입력되지 않았습니다.")
	private String name;

	@NotBlank(message = "아이템 영어 이름이 입력되지 않았습니다.")
	private String englishName;

	@NotNull(message = "가격이 입력되지 않았습니다.")
	@Min(value = 0, message = "가격은 0원 이상이여야합니다.")
	private Integer price;

	@URL(message = "이미지가 잘못된 url 형식입니다.")
	@NotBlank(message = "이미지가 입력되지 않았습니다.")
	private String image;

	@NotBlank(message = "상품 설명이 입력되지않았습니다.")
	private String description;

	@NotNull(message = "아이템 default 옵션이 입력되지않았습니다.")
	private DefaultOptionCreateRequest defaultOptionRequest;

}