package com.os.common.enums;

public enum OrderStatusEnum {

    STATUS_NONE(0, "未确认"),
    STATUS_PASS(1, "已确认"),
    STATUS_BACK(2, "返单");

    private final Integer code;
    private final String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    OrderStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
