package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpOrderPicEntity;

import java.util.Map;

/**
 * 订单图片表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
public interface ExpOrderPicService extends IService<ExpOrderPicEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

