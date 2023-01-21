package com.prgrms.bdbks.domain.item.dto;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultOptionCreateRequest {

	private Integer espressoShotCount;

	private Integer vanillaSyrupCount;

	private Integer classicSyrupCount;

	private Integer hazelnutSyrupCount;

	private BeverageOption.Milk milkType;

	private BeverageOption.Coffee espressoType;

	private BeverageOption.MilkAmount milkAmount;

}
