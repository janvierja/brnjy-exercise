package com.barrenjoey.java.bank;

import org.apache.commons.lang3.StringUtils;

public enum AccountActionEnum {
    DEPOSIT("deposit"),
    WITHDRAW("withdraw"),
    UNKNOWN("");

    public final String type;

    AccountActionEnum(String type) {
        this.type = type;
    }

    public final String type() {
        return this.type;
    }

    public static AccountActionEnum decode(String type) {
        if(StringUtils.isEmpty(type)) {
            return UNKNOWN;
        }

        String typ = type.toLowerCase().trim();
        switch(typ) {
            case "deposit":
                return DEPOSIT;
            case "withdraw":
                return WITHDRAW;
            default:
                return UNKNOWN;
        }
    }
}
