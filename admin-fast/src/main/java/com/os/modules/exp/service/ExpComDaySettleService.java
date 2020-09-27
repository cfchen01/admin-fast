package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpComDaySettleEntity;
import com.os.modules.exp.entity.ExpDepartmentEntity;

import java.time.LocalDate;
import java.util.List;
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
     * @param settleDto
     * @return
     */
    ExpComDaySettleEntity getSettleDetail(SettleDto settleDto);

    /**
     * 根据结算类型结算报表
     * @param settleDto
     * @return
     */
    ExpComDaySettleEntity getDeliverSettle(SettleDto settleDto);

    /**
     * 修改公司日常支出
     * @param params
     * @return
     */
    Boolean updateDailyExpenses(Map<String, Object> params);

    /**
     * 财务核算
     * @param settleDto
     * @return
     */
    Boolean updateSettle(SettleDto settleDto);

}

