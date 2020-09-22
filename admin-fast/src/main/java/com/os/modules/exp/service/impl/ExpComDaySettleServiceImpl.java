package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.enums.SettleStatusEnum;
import com.os.common.utils.UserUtils;
import com.os.modules.exp.entity.ExpExpensesDetailEntity;
import com.os.modules.exp.service.ExpExpensesDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpComDaySettleDao;
import com.os.modules.exp.entity.ExpComDaySettleEntity;
import com.os.modules.exp.service.ExpComDaySettleService;
import org.springframework.transaction.annotation.Transactional;


@Service("expComDaySettleService")
public class ExpComDaySettleServiceImpl extends ServiceImpl<ExpComDaySettleDao, ExpComDaySettleEntity> implements ExpComDaySettleService {

    @Autowired
    private ExpExpensesDetailService expExpensesDetailService;

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

    @Transactional
    @Override
    public Boolean updateDailyExpenses(LocalDate deliverDate, Integer money) {
        ExpComDaySettleEntity expComDaySettleEntity = this.getByDeliverDate(deliverDate);
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

        expExpensesDetailService.save(expExpensesDetailEntity);

        return this.saveOrUpdate(expComDaySettleEntity);
    }
}
