package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpSettleEntity;

import java.util.Map;

/**
 * 结算方式
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpSettleService extends IService<ExpSettleEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

