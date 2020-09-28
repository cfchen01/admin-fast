package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.enums.OrderStatusEnum;
import com.os.common.enums.SettleStatusEnum;
import com.os.common.exception.RRException;
import com.os.common.utils.UserUtils;
import com.os.modules.exp.dao.ExpOrderDao;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.*;
import com.os.modules.exp.service.*;
import com.os.modules.exp.vo.OrderResumeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpComDaySettleDao;
import org.springframework.transaction.annotation.Transactional;


@Service("expComDaySettleService")
public class ExpComDaySettleServiceImpl extends ServiceImpl<ExpComDaySettleDao, ExpComDaySettleEntity> implements ExpComDaySettleService {

    @Autowired
    private ExpExpensesDetailService expExpensesDetailService;

    @Autowired
    private ExpOrderDao expOrderDao;

    @Autowired
    private ExpOrderService expOrderService;

    @Autowired
    private ExpDepDaySettleService expDepDaySettleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpComDaySettleEntity> page = this.page(
                new Query<ExpComDaySettleEntity>().getPage(params),
                new LambdaQueryWrapper<ExpComDaySettleEntity>()

        );

        return new PageUtils(page);
    }

    @Override
    public ExpComDaySettleEntity getByDeliverDate(LocalDate deliverDate) {
        return this.lambdaQuery().eq(ExpComDaySettleEntity::getDeliverDate, deliverDate).one();
    }

    @Override
    public ExpComDaySettleEntity getSettleDetail(SettleDto settleDto) {
        ExpComDaySettleEntity expComDaySettleEntity = new ExpComDaySettleEntity();
        if (0 == settleDto.getType()) {
            expComDaySettleEntity = this.lambdaQuery().eq(ExpComDaySettleEntity::getDeliverDate, settleDto.getDeliverDate())
                    .eq(ExpComDaySettleEntity::getStatus, SettleStatusEnum.STATUS_PASS.getCode())
                    .one();
        } else if (1 == settleDto.getType()) {
            expComDaySettleEntity = baseMapper.getMonthSettle(settleDto);
        }
        if (expComDaySettleEntity == null) {
            expComDaySettleEntity = new ExpComDaySettleEntity();
            expComDaySettleEntity.setStatus(SettleStatusEnum.STATUS_NONE.getCode());
        }
        return expComDaySettleEntity;
    }

    @Override
    public ExpComDaySettleEntity getDeliverSettle(SettleDto settleDto) {
        ExpComDaySettleEntity expComDaySettleEntity = new ExpComDaySettleEntity();
        if (0 == settleDto.getType()) {
            expComDaySettleEntity = this.lambdaQuery().eq(ExpComDaySettleEntity::getDeliverDate, settleDto.getDeliverDate()).one();
            //已结算直接返回
            if (expComDaySettleEntity != null && SettleStatusEnum.STATUS_PASS.getCode().equals(expComDaySettleEntity.getStatus())) {
                expComDaySettleEntity.setCanSubmit(false);
                return expComDaySettleEntity;
            }
        } else if (1 == settleDto.getType()) {
            //只统计已结算的天数
            expComDaySettleEntity = baseMapper.getMonthSettle(settleDto);
            if (expComDaySettleEntity == null) {
                throw new RRException("当前时间尚未结算");
            } else {
                return expComDaySettleEntity;
            }
        }
        if (expComDaySettleEntity == null) {
            if (settleDto.getDeliverDate().length() != 10) {
                throw new RRException("当前时间尚未结算");
            }
            expComDaySettleEntity = new ExpComDaySettleEntity();
            expComDaySettleEntity.setStatus(SettleStatusEnum.STATUS_NONE.getCode());
            expComDaySettleEntity.setDeliverDate(LocalDate.parse(settleDto.getDeliverDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            expComDaySettleEntity.setDeliverMonth(settleDto.getDeliverDate().substring(0, 7));
            expComDaySettleEntity.setDailyExpenses(0);

            this.save(expComDaySettleEntity);
        }

        /**
         * 获取某天核算账单
         */
        expComDaySettleEntity = this.convertSettle(settleDto);
        try {
            expComDaySettleEntity.setCanSubmit(this.checkSettle(settleDto));
        } catch (Exception e){
            expComDaySettleEntity.setCanSubmit(false);
        }

        return expComDaySettleEntity;
    }

    @Transactional
    @Override
    public Boolean updateDailyExpenses(Map<String, Object> params) {
        String dateStr = MapUtils.mstr(params, "deliverDate");
        String expDesc = MapUtils.mstr(params, "expDesc");
        String moneyStr = MapUtils.mstr(params, "money");
        Integer money = 0;

        if (StringUtils.isEmpty(dateStr)) {
            throw new RRException("输入参数错误");
        }
        try {
            money = Integer.parseInt(moneyStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("输入金额错误");
        }
        LocalDate deliverDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ExpComDaySettleEntity expComDaySettleEntity = this.getByDeliverDate(deliverDate);
        if (SettleStatusEnum.STATUS_PASS.getCode().equals(expComDaySettleEntity.getStatus())) {
            throw new RRException("当前日期已结算，不允许修改");
        }
        if (expComDaySettleEntity == null) {
            expComDaySettleEntity = new ExpComDaySettleEntity();
            expComDaySettleEntity.setDeliverDate(deliverDate);
            expComDaySettleEntity.setDailyExpenses(money);
            expComDaySettleEntity.setStatus(SettleStatusEnum.STATUS_NONE.getCode());
            expComDaySettleEntity.setUserId(UserUtils.getUserId());
        } else {
            Integer count = expComDaySettleEntity.getDailyExpenses() == null?0:expComDaySettleEntity.getDailyExpenses();
            expComDaySettleEntity.setDailyExpenses(count + money);
        }

        //添加支出明细
        ExpExpensesDetailEntity expExpensesDetailEntity = new ExpExpensesDetailEntity();
        expExpensesDetailEntity.setCreateDate(deliverDate);
        expExpensesDetailEntity.setCreateTime(new Date());
        expExpensesDetailEntity.setUserId(UserUtils.getUserId());
        expExpensesDetailEntity.setMoney(money);
        expExpensesDetailEntity.setExpDesc(expDesc);

        expExpensesDetailService.save(expExpensesDetailEntity);

        return this.saveOrUpdate(expComDaySettleEntity);
    }

    /**
     * 核算账单入库
     * @param settleDto
     * @return
     */
    @Override
    public Boolean updateSettle(SettleDto settleDto) {
        ExpComDaySettleEntity expComDaySettleEntity = this.convertSettle(settleDto);
        expComDaySettleEntity.setCanSubmit(this.checkSettle(settleDto));

        if (expComDaySettleEntity.getCanSubmit()) {
            expComDaySettleEntity.setStatus(SettleStatusEnum.STATUS_PASS.getCode());
            this.updateById(expComDaySettleEntity);
        }
        return true;
    }

    /**
     * 校验是否可以核算
     * @param settleDto
     * @return
     */
    private Boolean checkSettle(SettleDto settleDto){
        if (StringUtils.isEmpty(settleDto.getDeliverDate())) {
            throw new RRException("输入参数错误");
        }

        LocalDate deliverDate = LocalDate.parse(settleDto.getDeliverDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ExpComDaySettleEntity expComDaySettleEntity = this.lambdaQuery().eq(ExpComDaySettleEntity::getDeliverDate, deliverDate).one();
        if (SettleStatusEnum.STATUS_PASS.getCode().equals(expComDaySettleEntity.getStatus())) {
            throw new RRException("当日账单已结算");
        }

        if(!LocalDate.now().isAfter(deliverDate)) {
            throw new RRException("只允许结算今日之前的时间");
        }

        ExpOrderEntity expOrderEntity = expOrderService.lambdaQuery().eq(ExpOrderEntity::getDeliverDate, deliverDate)
                .eq(ExpOrderEntity::getStatus, OrderStatusEnum.STATUS_NONE.getCode()).one();
        if (expOrderEntity != null) {
            throw new RRException("当日订单尚未确认完，不允许结算");
        }

        OrderResumeVo map = expOrderDao.getOrderResume(settleDto);
        //垫费
        Integer advance = MapUtils.oint(map.getAdvance());
        //已收垫费
        Integer advanceIn = MapUtils.oint(map.getAdvanceIn());
        if (!advance.equals(advanceIn)) {
            throw new RRException("当日垫费尚未收齐，不允许结算");
        }

        ExpDepDaySettleEntity expDepDaySettleEntity = expDepDaySettleService.lambdaQuery().eq(ExpDepDaySettleEntity::getDeliverDate, deliverDate)
                .eq(ExpDepDaySettleEntity::getStatus, SettleStatusEnum.STATUS_NONE.getCode()).one();
        if (expDepDaySettleEntity != null ) {
            throw new RRException("当日网点尚未结算完，不允许结算");
        }
        return true;
    }

    /**
     * 核算某天账单
     * @param settleDto
     * @return
     */
    private ExpComDaySettleEntity convertSettle(SettleDto settleDto){
        ExpComDaySettleEntity expComDaySettleEntity = this.lambdaQuery().eq(ExpComDaySettleEntity::getDeliverDate, settleDto.getDeliverDate()).one();
        OrderResumeVo  map = expOrderDao.getOrderResume(settleDto);

        //垫费
        Integer advance = 0;
        //已收垫费
        Integer advanceIn = 0;
        //未收垫费
        Integer advanceNot = advance - advanceIn;
        //运费
        Integer freight = 0;
        //送货费
        Integer delivery = 0;


        if (Objects.nonNull(map)) {
            freight = MapUtils.oint(map.getFreight());
            delivery = MapUtils.oint(map.getDelivery());
            advance = MapUtils.oint(map.getAdvance());
            advanceIn = MapUtils.oint(map.getAdvanceIn());
        }

        expComDaySettleEntity.setComDelivery(delivery);
        expComDaySettleEntity.setComAdvance(advance);
        expComDaySettleEntity.setFreightIncome(freight);
        expComDaySettleEntity.setTotalExpenses(expComDaySettleEntity.getDailyExpenses() + advanceNot);
        expComDaySettleEntity.setTotalIncome(freight + advanceIn);
        expComDaySettleEntity.setProfit(expComDaySettleEntity.getTotalIncome() - expComDaySettleEntity.getTotalExpenses());
        return expComDaySettleEntity;
    }
}
