package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpOrderPicDao;
import com.os.modules.exp.entity.ExpOrderPicEntity;
import com.os.modules.exp.service.ExpOrderPicService;


@Service("expOrderPicService")
public class ExpOrderPicServiceImpl extends ServiceImpl<ExpOrderPicDao, ExpOrderPicEntity> implements ExpOrderPicService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpOrderPicEntity> page = this.page(
                new Query<ExpOrderPicEntity>().getPage(params),
                new LambdaQueryWrapper<ExpOrderPicEntity>()

        );

        return new PageUtils(page);
    }

}
