package com.os.modules.exp.dao;

import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpUserMoneyEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 业务员提货单费用表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@Mapper
@Repository
public interface ExpUserMoneyDao extends BaseMapper<ExpUserMoneyEntity> {

    Map<String, Object> getUserSettle(SettleDto settleDto);

}
