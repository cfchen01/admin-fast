package com.os.modules.exp.vo;

import lombok.Data;

@Data
public class OrderResumeVo {

    //运费
    private Integer freight;
    //送货费
    private Integer delivery;
    //垫费
    private Integer advance;
    //已收垫费
    private Integer advanceIn;
}
