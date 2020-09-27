package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpDepartmentEntity;

import java.util.List;
import java.util.Map;

/**
 * 网点表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpDepartmentService extends IService<ExpDepartmentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ExpDepartmentEntity> queryAll();
}

