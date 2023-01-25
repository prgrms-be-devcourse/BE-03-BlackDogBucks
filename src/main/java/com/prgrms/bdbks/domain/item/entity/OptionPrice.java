package com.prgrms.bdbks.domain.item.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 2023/01/25 마지막에 시간남으면 jdbc화
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionPrice {

	private int espressoPrice = 600;

	private int syrupPrice = 600;

}
