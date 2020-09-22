package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpPackingEntity;

import java.util.Map;

/**
 * 包装表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpPackingService extends IService<ExpPackingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

