package com.os.modules.exp.dto;

import lombok.Data;

@Data
public class SettleDto {

    //结算日期
    private String deliverDate;
    //网点
    private Integer deptId;
    //核算类型0、日 1、月
    private Integer type;
    //结算类型
    private String settleCode;
    //订单状态
    private Integer status;
    //用户id
    private Long userId;
}
