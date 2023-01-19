package com.prgrms.bdbks.domain.item.entity;

import static com.google.common.base.Preconditions.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.global.util.UrlValidator;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends AbstractTimeColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@NotNull
	@Column(name = "english_name", nullable = false, length = 50)
	private String englishName;

	@NotNull
	@Column(name = "price", nullable = false)
	@ColumnDefault("0")
	private int price = 0;

	@NotNull
	@Column(name = "image", nullable = false)
	private String image;

	@NotNull
	@Column(name = "is_best", nullable = false, columnDefinition = "bit")
	private boolean isBest = false;

	@NotNull
	@Column(name = "is_new", nullable = false, columnDefinition = "bit")
	private boolean isNew = false;

	@NotNull
	@Column(name = "description", nullable = false)
	private String description;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private ItemCategory category;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "default_option_id")
	private DefaultOption defaultOption;

	@Builder
	public Item(String name, ItemCategory category, String englishName, int price, String image,
		String description) {
		validateName(name);
		checkNotNull(category);
		validateEnglishName(englishName);
		validatePrice(price);
		validateDescription(description);
		validateImage(image);
		this.name = name;
		this.category = category;
		this.englishName = englishName;
		this.price = price;
		this.image = image;
		this.description = description;
	}

	private void validateName(String name) {
		checkArgument(StringUtils.hasText(name) && !name.isBlank(), "name 은 빈 값이면 안됩니다.");
		checkArgument(name.length() <= 50,
			"name 의 글자수는 50자 이하여야 합니다.");
	}

	private void validateEnglishName(String englishName) {
		checkArgument(StringUtils.hasText(englishName) && !englishName.isBlank(), "englishName 은 빈 값이면 안됩니다.");
		checkArgument(englishName.length() <= 50,
			"englishName 의 글자수는 50자 이하여야 합니다.");
	}

	private void validatePrice(int price) {
		checkArgument(price >= 0, "price 는 항상 0 이상 이여야 합니다.");
	}

	private void validateDescription(String description) {
		checkArgument(StringUtils.hasText(description) && !description.isBlank(), "description 은 빈 값이면 안됩니다.");
		checkArgument(description.length() <= 255, "description 의 글자수는 1자 이상 50자 이하여야 합니다.");
	}

	private void validateImage(String image) {
		checkArgument(UrlValidator.isValid(image), "image URL 은 유효한 URL 이여야 합니다.");
	}

}
