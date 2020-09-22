package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpDepDaySettleDao;
import com.os.modules.exp.entity.ExpDepDaySettleEntity;
import com.os.modules.exp.service.ExpDepDaySettleService;


@Service("expDepDaySettleService")
public class ExpDepDaySettleServiceImpl extends ServiceImpl<ExpDepDaySettleDao, ExpDepDaySettleEntity> implements ExpDepDaySettleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpDepDaySettleEntity> page = this.page(
                new Query<ExpDepDaySettleEntity>().getPage(params),
                new LambdaQueryWrapper<ExpDepDaySettleEntity>()

        );

        return new PageUtils(page);
    }

    @Override
    public ExpDepDaySettleEntity getByDeliverDate(LocalDate deliverDate) {
        return this.lambdaQuery().eq(ExpDepDaySettleEntity::getDeliverDate, deliverDate).one();
    }
}
