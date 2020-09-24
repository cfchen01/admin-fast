package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public List<ExpExpensesDetailEntity> getExpExpensesDetailList(Map<String, Object> params) {
        String keyword = MapUtils.mstr(params, "keyword");
        String type = MapUtils.mstr(params, "type");
        List<ExpExpensesDetailEntity> list = new ArrayList<>();
        if ("day".equals(type)) {
            list = this.lambdaQuery().eq(ExpExpensesDetailEntity::getCreateDate, keyword).list();
        } else if ("month".equals(type)){
            LocalDate date = LocalDate.parse(keyword + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate firstday = date.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate lastDay  = date.with(TemporalAdjusters.lastDayOfMonth());
            Map map = new HashMap();
            map.put("sDate", firstday);
            map.put("eDate", lastDay);
            list = baseMapper.getMonthList(map);
        }
        return list;
    }
}
