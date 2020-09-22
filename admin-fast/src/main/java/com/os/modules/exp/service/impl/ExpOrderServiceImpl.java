package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.enums.OrderStatusEnum;
import com.os.common.enums.SettleStatusEnum;
import com.os.common.exception.RRException;
import com.os.common.utils.UserUtils;
import com.os.modules.exp.entity.ExpComDaySettleEntity;
import com.os.modules.exp.entity.ExpDepDaySettleEntity;
import com.os.modules.exp.entity.ExpUserMoneyEntity;
import com.os.modules.exp.service.ExpComDaySettleService;
import com.os.modules.exp.service.ExpDepDaySettleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpOrderDao;
import com.os.modules.exp.entity.ExpOrderEntity;
import com.os.modules.exp.service.ExpOrderService;


@Service("expOrderService")
public class ExpOrderServiceImpl extends ServiceImpl<ExpOrderDao, ExpOrderEntity> implements ExpOrderService {

    @Autowired
    private ExpComDaySettleService expComDaySettleService;

    @Autowired
    private ExpDepDaySettleService expDepDaySettleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpOrderEntity> page = this.page(
                new Query<ExpOrderEntity>().getPage(params),
                new LambdaQueryWrapper<ExpOrderEntity>()
                    .eq(MapUtils.mint(params, "settleId") != null, ExpOrderEntity::getSettleId, MapUtils.mint(params, "settleId"))
                    .eq(MapUtils.mint(params, "status") != null, ExpOrderEntity::getStatus, MapUtils.mint(params, "status"))
                    .eq(StringUtils.isNotEmpty(MapUtils.mstr(params, "deliverDate")), ExpOrderEntity::getDeliverDate, MapUtils.mint(params, "deliverDate"))

        );

        return new PageUtils(page);
    }

    @Override
    public Boolean saveOrder(ExpOrderEntity expOrderEntity) {
        expOrderEntity.setUserId(UserUtils.getUserId());
        expOrderEntity.setStatus(OrderStatusEnum.STATUS_NONE.getCode());
        expOrderEntity.setCreateTime(new Date());

        return this.save(expOrderEntity);
    }

    @Override
    public Boolean updateOrder(ExpOrderEntity expOrderEntity) {

        //公司当日已结算不允许修改
        ExpComDaySettleEntity expComDaySettleEntity = expComDaySettleService.getByDeliverDate(expOrderEntity.getDeliverDate());
        if (expComDaySettleEntity != null && SettleStatusEnum.STATUS_PASS.getCode().equals(expComDaySettleEntity.getStatus())) {
            throw  new RRException("[" + expOrderEntity.getDeliverDate() + "]财务已结算，不允许修改");
        }
        //当前网点时间已结算不允许修改
        ExpDepDaySettleEntity expDepDaySettleEntity = expDepDaySettleService.getByDeliverDate(expOrderEntity.getDeliverDate());
        if (expDepDaySettleEntity != null && SettleStatusEnum.STATUS_PASS.getCode().equals(expDepDaySettleEntity.getStatus())
        && expDepDaySettleEntity.getDeptId().equals(expOrderEntity.getDeptId())) {
            throw  new RRException("[" + expOrderEntity.getDeliverDate() + "]网点已结算，不允许修改");
        }
        //非未确认订单不允许修改
        if (!OrderStatusEnum.STATUS_NONE.equals(expOrderEntity.getStatus())) {
            throw  new RRException("当前订单状态不允许修改");
        }

        return this.updateById(expOrderEntity);
    }

    @Override
    public ExpOrderEntity getByCode(String ordCode) {
        return this.lambdaQuery().eq(ExpOrderEntity::getOrdCode, ordCode).one();
    }

    @Override
    public Integer getUserMoneyInTf(ExpUserMoneyEntity expUserMoneyEntity) {
        return null;
    }
}
