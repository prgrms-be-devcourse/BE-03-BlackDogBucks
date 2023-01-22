package com.prgrms.bdbks.domain.item.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.Preconditions;
import com.prgrms.bdbks.domain.item.entity.BeverageOption.Coffee;
import com.prgrms.bdbks.domain.item.entity.BeverageOption.Milk;
import com.prgrms.bdbks.domain.item.entity.BeverageOption.MilkAmount;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "default_options")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "espresso_shot_count", columnDefinition = "tinyint")
	private Integer espressoShotCount;

	@Column(name = "vanilla_syrup_count", columnDefinition = "tinyint")
	private Integer vanillaSyrupCount;

	@Column(name = "classic_syrup_count", columnDefinition = "tinyint")
	private Integer classicSyrupCount;

	@Column(name = "hazelnut_syrup_count", columnDefinition = "tinyint")
	private Integer hazelnutSyrupCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "milk_type", length = 20)
	private Milk milkType = null;

	@Enumerated(EnumType.STRING)
	@Column(name = "espresso_Type", length = 20)
	private Coffee espressoType = null;

	@Enumerated(EnumType.STRING)
	@Column(name = "milk_amount", length = 20)
	private MilkAmount milkAmount = null;

	@Builder
	protected DefaultOption(Integer espressoShotCount, Integer vanillaSyrupCount, Integer classicSyrupCount,
		Integer hazelnutSyrupCount, Milk milkType, Coffee espressoType, MilkAmount milkAmount) {

		validateCount(hazelnutSyrupCount);
		validateCount(espressoShotCount);
		validateCount(classicSyrupCount);
		validateCount(vanillaSyrupCount);

		this.espressoShotCount = espressoShotCount;
		this.vanillaSyrupCount = vanillaSyrupCount;
		this.classicSyrupCount = classicSyrupCount;
		this.hazelnutSyrupCount = hazelnutSyrupCount;
		this.milkType = milkType;
		this.espressoType = espressoType;
		this.milkAmount = milkAmount;
	}

	private void validateCount(Integer count) {
		if (Objects.isNull(count)) {
			return;
		}

		Preconditions.checkArgument(count >= 0 && count <= 9, "Option 개수는 9보다 작은 양수여야합니다.");
	}

}