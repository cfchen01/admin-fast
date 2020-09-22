package com.os.modules.exp.dao;

import com.os.modules.exp.entity.ExpUserMoneyEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务员提货单费用表
 * 
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Mapper
public interface ExpUserMoneyDao extends BaseMapper<ExpUserMoneyEntity> {
	
}
