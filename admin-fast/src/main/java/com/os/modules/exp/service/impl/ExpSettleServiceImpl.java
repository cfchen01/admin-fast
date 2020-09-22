package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpSettleDao;
import com.os.modules.exp.entity.ExpSettleEntity;
import com.os.modules.exp.service.ExpSettleService;


@Service("expSettleService")
public class ExpSettleServiceImpl extends ServiceImpl<ExpSettleDao, ExpSettleEntity> implements ExpSettleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpSettleEntity> page = this.page(
                new Query<ExpSettleEntity>().getPage(params),
                new LambdaQueryWrapper<ExpSettleEntity>().like(StringUtils.isNotEmpty(MapUtils.mstr(params, "key")), ExpSettleEntity::getSettleName, MapUtils.mstr(params, "key"))

        );

        return new PageUtils(page);
    }

}
