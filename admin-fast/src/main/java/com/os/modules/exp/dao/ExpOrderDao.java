package com.os.modules.exp.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.os.modules.exp.entity.ExpOrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 订单表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Mapper
public interface ExpOrderDao extends BaseMapper<ExpOrderEntity> {

    IPage<ExpOrderEntity> selectOrderPage(Page pager, @Param("param") Map<String, Object> param);

    Integer getResume(@Param("param") Map<String, Object> param);

}
