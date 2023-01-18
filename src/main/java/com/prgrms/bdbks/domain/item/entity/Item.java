package com.prgrms.bdbks.domain.item.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.global.util.UrlValidator;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;


@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends AbstractTimeColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotNull
    @Column(name = "eng_name", nullable = false, length = 50)
    private String engName;

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
    public Item(String name, ItemCategory category, String engName, int price, String image,
        String description) {
        validateName(name);
        checkNotNull(category);
        validateEngName(engName);
        validatePrice(price);
        validateDescription(description);
        validateImage(image);
        this.name = name;
        this.category = category;
        this.engName = engName;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    private void validateName(String name) {
        checkArgument(StringUtils.hasText(name) && !name.isBlank(), "name must not be empty.");
        checkArgument( name.length() <= 50,
            "name must be less than 50 characters.");
    }

    private void validateEngName(String engName) {
        checkArgument(StringUtils.hasText(engName) && !engName.isBlank(), "engName must not be empty.");
        checkArgument(engName.length() <= 50,
            "engName must be less than 50 characters.");
    }

    private void validatePrice(int price) {
        checkArgument(price >= 0, "price must always be greater than 0 ");
    }

    private void validateDescription(String description) {
        checkArgument(StringUtils.hasText(description) && !description.isBlank(),"description must not be empty." );
        checkArgument(description.length() <= 255, "description must be greater than 1 character and less than 50 characters.");
    }

    private void validateImage(String image) {
        checkArgument(UrlValidator.isValid(image),"image URL is must be valid URL");
    }

}
