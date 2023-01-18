package com.prgrms.bdbks.domain.item.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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
    @Column(name = "eng_name", nullable = false)
    private String engName;

    @NotNull
    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @Builder
    protected ItemCategory(String name, String engName, ItemType itemType) {
        validateName(name);
        validateEngName(engName);
        checkNotNull(itemType);
        this.name = name;
        this.engName = engName;
        this.itemType = itemType;
    }

    private void validateEngName(String engName) {
        checkArgument(StringUtils.hasText(engName) && !engName.isBlank(), "engName must not be empty.");
        checkArgument(engName.length() >= 1 && engName.length() <= 30,
            "engName must be greater than 1 character and less than 30 characters.");
    }

    private void validateName(String name) {
        checkArgument(StringUtils.hasText(name) && !name.isBlank(), "name must not be empty.");
        checkArgument(name.length() >= 1 && name.length() <= 30,
            "name must be greater than 1 character and less than 30 characters.");
    }

}
