package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpGoodsEntity;

import java.util.Map;

/**
 * 货物表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpGoodsService extends IService<ExpGoodsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

