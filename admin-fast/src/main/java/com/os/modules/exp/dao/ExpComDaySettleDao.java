package com.os.modules.exp.dao;

import com.os.modules.exp.entity.ExpComDaySettleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 公司日结报表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Mapper
public interface ExpComDaySettleDao extends BaseMapper<ExpComDaySettleEntity> {

    ExpComDaySettleEntity getMonthSettle(Map<String, Object> params);

}
