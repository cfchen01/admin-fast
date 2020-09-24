package com.os.modules.exp.vo;

import com.os.modules.exp.entity.ExpDepartmentEntity;
import com.os.modules.exp.entity.ExpGoodsEntity;
import com.os.modules.exp.entity.ExpPackingEntity;
import com.os.modules.exp.entity.ExpSettleEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderObjVo {

    private List<ExpSettleEntity> settleList;
    private List<ExpPackingEntity> packingList;
    private List<ExpGoodsEntity> goodsList;
    private List<ExpDepartmentEntity> deptList;
}
