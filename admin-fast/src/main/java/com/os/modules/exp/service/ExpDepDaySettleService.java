package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpDepDaySettleEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 网点日结报表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpDepDaySettleService extends IService<ExpDepDaySettleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    ExpDepDaySettleEntity getByDeliverDate(LocalDate deliverDate, Integer deptId);

    ExpDepDaySettleEntity getDeptSettle(SettleDto settleDto);

    List<ExpDepDaySettleEntity> getSettleList(SettleDto settleDto);

    /**
     * 修改公司日常支出
     * @param params
     * @return
     */
    Boolean updateComMoneyIn(Map<String, Object> params);

    Boolean updateSettle(Integer id);
}

