package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpComDaySettleEntity;

import java.time.LocalDate;
import java.util.Map;

/**
 * 公司日结报表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpComDaySettleService extends IService<ExpComDaySettleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据结算日期查询报表
     * @param deliverDate
     * @return
     */
    ExpComDaySettleEntity getByDeliverDate(LocalDate deliverDate);

    /**
     * 根据结算类型查询报表
     * @param params
     * @return
     */
    ExpComDaySettleEntity getByDeliverType(Map<String, Object> params);

    /**
     * 修改公司日常支出
     * @param deliverDate
     * @param money
     * @return
     */
    Boolean updateDailyExpenses(LocalDate deliverDate, Integer money);
}

