package com.prgrms.bdbks.domain.order.entity;

import static com.google.common.base.Preconditions.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "custom_options")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomOption extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "custom_option_id")
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
	private BeverageOption.Milk milkType = null;

	@Enumerated(EnumType.STRING)
	@Column(name = "espresso_Type", length = 20)
	private BeverageOption.Coffee espressoType = null;

	@Enumerated(EnumType.STRING)
	@Column(name = "milk_amount", length = 20)
	private BeverageOption.MilkAmount milkAmount = null;

	@Enumerated(EnumType.STRING)
	@Column(name = "cup_size", length = 10)
	@NotNull
	private BeverageOption.Size cupSize;

	@Enumerated(EnumType.STRING)
	@Column(name = "cup_type", length = 10)
	@NotNull
	private BeverageOption.CupType cupType;

	@Builder
	protected CustomOption(Integer espressoShotCount, Integer vanillaSyrupCount, Integer classicSyrupCount,
		Integer hazelnutSyrupCount, BeverageOption.Milk milkType, BeverageOption.Coffee espressoType,
		BeverageOption.MilkAmount milkAmount, BeverageOption.Size cupSize, BeverageOption.CupType cupType) {

		checkNotNull(cupType, "cupType 은 null 일 수 없습니다.");
		checkNotNull(cupSize, "cupSize 는 null 일 수 없습니다.");

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
		this.cupSize = cupSize;
		this.cupType = cupType;
	}

	private void validateCount(int count) {
		checkArgument(count >= 0 && count <= 9, "Option 개수는 9보다 작은 양수여야합니다.");
	}

}
