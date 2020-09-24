package com.os.modules.exp.dao;

import com.os.modules.exp.entity.ExpExpensesDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 公司支出明细表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Mapper
public interface ExpExpensesDetailDao extends BaseMapper<ExpExpensesDetailEntity> {

    List<ExpExpensesDetailEntity> getMonthList(Map<String, Object> params);

}
