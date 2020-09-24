package com.os.modules.exp.dao;

import com.os.modules.exp.entity.ExpOrderPicEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 订单图片表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
@Mapper
@Repository
public interface ExpOrderPicDao extends BaseMapper<ExpOrderPicEntity> {

}
