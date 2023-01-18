package com.prgrms.bdbks.domain.item.entity;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "categories")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Column(name = "english_name", nullable = false)
	private String englishName;

	@NotNull
	@Column(name = "item_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ItemType itemType;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Item> items = new ArrayList<>();

	@Builder
	protected ItemCategory(String name, String englishName, ItemType itemType) {
		validateName(name);
		validateEnglishName(englishName);
		checkNotNull(itemType);
		this.name = name;
		this.englishName = englishName;
		this.itemType = itemType;
	}

	private void validateEnglishName(String englishName) {
		checkArgument(StringUtils.hasText(englishName) && !englishName.isBlank(), "englishName 은 빈 값이면 안됩니다.");
		checkArgument(englishName.length() >= 1 && englishName.length() <= 30,
			"englishName 의 글자수는 1자 이상 50자 이하여야 합니다.");
	}

	private void validateName(String name) {
		checkArgument(StringUtils.hasText(name) && !name.isBlank(), "name 은 빈 값이면 안됩니다.");
		checkArgument(name.length() >= 1 && name.length() <= 30,
			"name 의 글자수는 1자 이상 50자 이하여야 합니다.");
	}

}
