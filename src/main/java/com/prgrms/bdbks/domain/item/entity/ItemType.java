package com.prgrms.bdbks.domain.item.entity;

import lombok.Getter;

@Getter
public enum ItemType {

    BEVERAGE("음료", "Beverage"),
    FOOD("푸드", "Food"),
    PRODUCT("상품", "Product");

    private final String korName;

    private final String engName;

    ItemType(String korName, String engName) {
        this.korName = korName;
        this.engName = engName;
    }

}
