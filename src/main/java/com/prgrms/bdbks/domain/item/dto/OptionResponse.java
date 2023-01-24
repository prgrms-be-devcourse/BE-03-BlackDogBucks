package com.prgrms.bdbks.domain.item.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class OptionResponse {

	private final CoffeeOption coffee;

	private List<SyrupsOption> syrup;

	private final MilkOption milk;

	private final List<SizeOption> size = Arrays.stream(BeverageOption.Size.values())
		.map(cupSize -> new SizeOption(cupSize.getEnglishName(), cupSize.getAmount()))
		.collect(Collectors.toList());

	private final List<String> cupType = Arrays.stream(BeverageOption.CupType.values())
		.map(BeverageOption.CupType::getKoreaName)
		.collect(Collectors.toList());

	public OptionResponse(BeverageOption.Coffee espressoType, Integer espressoShotCount, Integer vanillaSyrupCount,
		Integer classicSyrupCount, Integer hazelnutSyrupCount, BeverageOption.Milk milkType,
		BeverageOption.MilkAmount milkAmount) {

		milk = milkType != null ? new MilkOption(milkType, milkAmount) : null;

		coffee = espressoType != null ? new CoffeeOption(espressoType, espressoShotCount) : null;

		if (vanillaSyrupCount != null || classicSyrupCount != null || hazelnutSyrupCount != null) {
			syrup = new ArrayList<>();
		}

		// TODO: 2023/01/24 refactoring 해야함

		if (vanillaSyrupCount != null) {
			syrup.add(new SyrupsOption(BeverageOption.Syrup.VANILLA.getKorName(), vanillaSyrupCount));
		}

		if (classicSyrupCount != null) {
			syrup.add(new SyrupsOption(BeverageOption.Syrup.CLASSIC.getKorName(), classicSyrupCount));
		}

		if (hazelnutSyrupCount != null) {
			syrup.add(new SyrupsOption(BeverageOption.Syrup.HAZELNUT.getKorName(), hazelnutSyrupCount));
		}

	}

	@Getter
	public static class CoffeeOption {

		private String defaultType;

		private Integer defaultEspressoCount;

		private List<String> options;

		public CoffeeOption(BeverageOption.Coffee espressoType, Integer espressoShotCount) {
			this.defaultType = espressoType.getKorName();

			this.options = Arrays.stream(BeverageOption.Coffee.values())
				.map(BeverageOption.Coffee::getKorName)
				.collect(Collectors.toList());

			this.defaultEspressoCount = espressoShotCount;
		}
	}

	@Getter
	@AllArgsConstructor
	public static class SyrupsOption {

		private String syrupName;

		private Integer defaultCount;

	}

	@Getter
	public static class MilkOption {

		private String defaultType;

		private List<String> options;

		private String defaultMilkAmountOptionName;

		private List<String> amountOptions;

		public MilkOption(BeverageOption.Milk defaultMilk, BeverageOption.MilkAmount defaultAmount) {
			this.defaultType = defaultMilk.getKorName();
			this.options = Arrays.stream(BeverageOption.Milk.values())
				.map(BeverageOption.Milk::getKorName)
				.collect(Collectors.toList());
			this.defaultMilkAmountOptionName = defaultAmount.getKorName();
			this.amountOptions = Arrays.stream(BeverageOption.MilkAmount.values())
				.map(BeverageOption.MilkAmount::getKorName)
				.collect(Collectors.toList());
		}
	}

	@Getter
	public static class SizeOption {
		private String name;
		private String amount;

		public SizeOption(String name, String amount) {
			this.name = name;
			this.amount = amount;
		}
	}

}
