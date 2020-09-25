package com.os.modules.exp.dao;

import com.os.modules.exp.entity.ExpUserDeptEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户网点关系表
 * 
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-24 19:17:49
 */
@Mapper
public interface ExpUserDeptDao extends BaseMapper<ExpUserDeptEntity> {
	
}
