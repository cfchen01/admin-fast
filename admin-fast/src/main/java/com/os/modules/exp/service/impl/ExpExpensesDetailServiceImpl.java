package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpExpensesDetailDao;
import com.os.modules.exp.entity.ExpExpensesDetailEntity;
import com.os.modules.exp.service.ExpExpensesDetailService;


@Service("expExpensesDetailService")
public class ExpExpensesDetailServiceImpl extends ServiceImpl<ExpExpensesDetailDao, ExpExpensesDetailEntity> implements ExpExpensesDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpExpensesDetailEntity> page = this.page(
                new Query<ExpExpensesDetailEntity>().getPage(params),
                new LambdaQueryWrapper<ExpExpensesDetailEntity>()

        );

        return new PageUtils(page);
    }

}
