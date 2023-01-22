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

	@NotNull
	private ItemType itemType;

	@NotBlank
	private String categoryName;

	@NotBlank
	private String name;

	@NotBlank
	private String englishName;

	@Min(0)
	private Integer price;

	@URL
	@NotBlank
	private String image;

	@NotBlank
	private String description;

	@NotNull
	private DefaultOptionCreateRequest defaultOption;

}
