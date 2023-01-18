package com.prgrms.bdbks.domain.item.entity;

import lombok.Getter;

public interface BeverageOption {

    @Getter
    enum Coffee implements BeverageOption {
        ESPRESSO("에스프레소", "Espresso"),
        DECAFFEINATED("디카페인", "Decaf"),
        DRIP("드립", "Drip");

        private final String korName;
        private final String engName;

        Coffee(String korName, String engName) {
            this.korName = korName;
            this.engName = engName;
        }
    }

    @Getter
    enum Syrup implements BeverageOption {
        CLASSIC("클래식 시럽", "Classic Syrup"),
        VANILLA("바닐라 시럽", "Vanilla Syrup"),
        HAZELNUT("헤이즐넛 시럽", "Hazelnut Syrup");

        private final String korName;
        private final String engName;

        Syrup(String korName, String engName) {
            this.korName = korName;
            this.engName = engName;
        }
    }

    @Getter
    enum Milk implements BeverageOption {
        NORMAL("일반", "Normal"),
        OAT("오트(귀리)", "Oau"),
        SOY("두유", "Soy"),
        LOW_FAT("저지방", "Low Fat");

        private final String korName;
        private final String engName;


        Milk(String korName, String engName) {
            this.korName = korName;
            this.engName = engName;
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
        TALL("Tall","355ml"),
        GRANDE("Grande","473ml"),
        VENTI("Venti", "591ml");

        private final String engName;
        private final String amount;

        Size(String engName, String amount) {
            this.amount = amount;
            this.engName = engName;
        }
    }

}
