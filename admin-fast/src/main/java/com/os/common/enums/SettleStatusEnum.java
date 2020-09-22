package com.os.common.enums;

public enum SettleStatusEnum {

    STATUS_NONE(0, "未结算"),
    STATUS_PASS(1, "已结算");

    private final Integer code;
    private final String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    SettleStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
