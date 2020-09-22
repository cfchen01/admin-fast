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

import com.os.modules.exp.dao.ExpDepartmentDao;
import com.os.modules.exp.entity.ExpDepartmentEntity;
import com.os.modules.exp.service.ExpDepartmentService;


@Service("expDepartmentService")
public class ExpDepartmentServiceImpl extends ServiceImpl<ExpDepartmentDao, ExpDepartmentEntity> implements ExpDepartmentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpDepartmentEntity> page = this.page(
                new Query<ExpDepartmentEntity>().getPage(params),
                new LambdaQueryWrapper<ExpDepartmentEntity>().like(StringUtils.isNotEmpty(MapUtils.mstr(params, "key")), ExpDepartmentEntity::getDeptName, MapUtils.mstr(params, "key"))

        );

        return new PageUtils(page);
    }

}
