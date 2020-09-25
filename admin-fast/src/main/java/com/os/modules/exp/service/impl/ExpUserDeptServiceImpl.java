package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpUserDeptDao;
import com.os.modules.exp.entity.ExpUserDeptEntity;
import com.os.modules.exp.service.ExpUserDeptService;


@Service("expUserDeptService")
public class ExpUserDeptServiceImpl extends ServiceImpl<ExpUserDeptDao, ExpUserDeptEntity> implements ExpUserDeptService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpUserDeptEntity> page = this.page(
                new Query<ExpUserDeptEntity>().getPage(params),
                new LambdaQueryWrapper<ExpUserDeptEntity>()

        );

        return new PageUtils(page);
    }

    @Override
    public void saveOrUpdate(Long userId, Integer deptId) {
        this.removeByMap(new MapUtils().put("user_id", userId));

        if (deptId != null) {
            ExpUserDeptEntity expUserDeptEntity = new ExpUserDeptEntity();
            expUserDeptEntity.setDeptId(deptId);
            expUserDeptEntity.setUserId(userId);

            this.save(expUserDeptEntity);
        }
    }

    @Override
    public List<ExpUserDeptEntity> queryByUser(Long userId) {
        return this.lambdaQuery().list();
    }
}
