package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpOrderEntity;
import com.os.modules.exp.entity.ExpUserMoneyEntity;
import com.os.modules.exp.vo.OrderObjVo;
import com.os.modules.exp.vo.OrderResumeVo;
import io.swagger.models.auth.In;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 订单表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpOrderService extends IService<ExpOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void download(HttpServletRequest request, HttpServletResponse response) throws Exception;

    ExpOrderEntity getDetailById(String id);

    Boolean saveOrder(ExpOrderEntity expOrderEntity);

    Boolean updateOrder(ExpOrderEntity expOrderEntity);

    ExpOrderEntity getByCode(String ordCode);

    //获取业务员提付订单总金额
    Integer getUserMoneyInTf(ExpUserMoneyEntity expUserMoneyEntity);

    OrderObjVo getOrderObjVo();

    Boolean updateStatus(Map<String, Object> params);

    OrderResumeVo getResume(Map<String, Object> params);


}

