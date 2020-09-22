package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpExpensesDetailEntity;

import java.util.Map;

/**
 * 公司支出明细表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpExpensesDetailService extends IService<ExpExpensesDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

