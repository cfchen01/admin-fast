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

import com.os.modules.exp.dao.ExpGoodsDao;
import com.os.modules.exp.entity.ExpGoodsEntity;
import com.os.modules.exp.service.ExpGoodsService;


@Service("expGoodsService")
public class ExpGoodsServiceImpl extends ServiceImpl<ExpGoodsDao, ExpGoodsEntity> implements ExpGoodsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpGoodsEntity> page = this.page(
                new Query<ExpGoodsEntity>().getPage(params),
                new LambdaQueryWrapper<ExpGoodsEntity>().like(StringUtils.isNotEmpty(MapUtils.mstr(params, "key")), ExpGoodsEntity::getGoodsName, MapUtils.mstr(params, "key"))

        );

        return new PageUtils(page);
    }

}
