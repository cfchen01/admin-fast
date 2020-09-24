package com.os.modules.exp.dao;

import com.os.modules.exp.entity.ExpFileEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件信息表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
@Mapper
@Repository
public interface ExpFileDao extends BaseMapper<ExpFileEntity> {

    List<ExpFileEntity> selectPicByOrder(@Param("orderId") Integer orderId);

}
