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

import com.os.modules.exp.dao.ExpPackingDao;
import com.os.modules.exp.entity.ExpPackingEntity;
import com.os.modules.exp.service.ExpPackingService;


@Service("expPackingService")
public class ExpPackingServiceImpl extends ServiceImpl<ExpPackingDao, ExpPackingEntity> implements ExpPackingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpPackingEntity> page = this.page(
                new Query<ExpPackingEntity>().getPage(params),
                new LambdaQueryWrapper<ExpPackingEntity>().like(StringUtils.isNotEmpty(MapUtils.mstr(params, "key")), ExpPackingEntity::getPackingName, MapUtils.mstr(params, "key"))

        );

        return new PageUtils(page);
    }

}
