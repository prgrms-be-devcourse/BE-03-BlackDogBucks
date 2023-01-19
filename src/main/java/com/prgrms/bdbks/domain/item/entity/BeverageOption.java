package com.prgrms.bdbks.domain.item.entity;

import lombok.Getter;

public interface BeverageOption {

    @Getter
    enum Coffee implements BeverageOption {
        ESPRESSO("에스프레소", "Espresso"),
        DECAFFEINATED("디카페인", "Decaf"),
        DRIP("드립", "Drip");

        private final String korName;
        private final String englishName;

        Coffee(String korName, String englishName) {
            this.korName = korName;
            this.englishName = englishName;
        }
    }

    @Getter
    enum Syrup implements BeverageOption {
        CLASSIC("클래식 시럽", "Classic Syrup"),
        VANILLA("바닐라 시럽", "Vanilla Syrup"),
        HAZELNUT("헤이즐넛 시럽", "Hazelnut Syrup");

        private final String korName;
        private final String englishName;

        Syrup(String korName, String englishName) {
            this.korName = korName;
            this.englishName = englishName;
        }
    }

    @Getter
    enum Milk implements BeverageOption {
        NORMAL("일반", "Normal"),
        OAT("오트(귀리)", "Oau"),
        SOY("두유", "Soy"),
        LOW_FAT("저지방", "Low Fat");

        private final String korName;
        private final String englishName;

        Milk(String korName, String englishName) {
            this.korName = korName;
            this.englishName = englishName;
        }
    }

    @Getter
    enum MilkAmount implements BeverageOption {
        MUCH("많이"),
        MEDIUM("보통"),
        LITTLE("적게");

        private final String korName;

        MilkAmount(String korName) {
            this.korName = korName;
        }
    }

    @Getter
    enum Size {
        TALL("Tall", "355ml"),
        GRANDE("Grande", "473ml"),
        VENTI("Venti", "591ml");

        private final String englishName;
        private final String amount;

        Size(String englishName, String amount) {
            this.amount = amount;
            this.englishName = englishName;
        }
    }

    @Getter
    enum CupType {
        PERSONAL("개인컵"),
        STORE("매장컵"),
        DISPOSABLE("일회용컵");

        private final String koreaName;

        CupType(String koreaName) {
            this.koreaName = koreaName;
        }

    }

}
