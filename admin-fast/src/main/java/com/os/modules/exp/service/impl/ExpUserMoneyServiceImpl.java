package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpUserMoneyDao;
import com.os.modules.exp.entity.ExpUserMoneyEntity;
import com.os.modules.exp.service.ExpUserMoneyService;


@Service("expUserMoneyService")
public class ExpUserMoneyServiceImpl extends ServiceImpl<ExpUserMoneyDao, ExpUserMoneyEntity> implements ExpUserMoneyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpUserMoneyEntity> page = this.page(
                new Query<ExpUserMoneyEntity>().getPage(params),
                new LambdaQueryWrapper<ExpUserMoneyEntity>()

        );

        return new PageUtils(page);
    }

    @Override
    public Boolean updateUserMoney(ExpUserMoneyEntity expUserMoneyEntity) {
        ExpUserMoneyEntity record = this.lambdaQuery()
                .eq(ExpUserMoneyEntity::getDaySettleId, expUserMoneyEntity.getDaySettleId())
                .eq(ExpUserMoneyEntity::getUserId, expUserMoneyEntity.getUserId()).one();

        if (record == null) {
            record = new ExpUserMoneyEntity();
            record.setDaySettleId(expUserMoneyEntity.getDaySettleId());
            record.setMoneyIn(expUserMoneyEntity.getMoneyIn());
            record.setDeliverDate(expUserMoneyEntity.getDeliverDate());
            record.setUserId(expUserMoneyEntity.getUserId());
            record.setDeptId(expUserMoneyEntity.getDeptId());
        }
        Integer count = record.getMoneyIn() == null?0:record.getMoneyIn();
        record.setMoneyIn(count + expUserMoneyEntity.getMoneyIn());
        return saveOrUpdate(record);
    }
}
