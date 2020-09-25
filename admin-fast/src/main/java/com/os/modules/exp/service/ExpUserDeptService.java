package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpUserDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户网点关系表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-24 19:17:49
 */
public interface ExpUserDeptService extends IService<ExpUserDeptEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveOrUpdate(Long userId, Integer deptId);

    List<ExpUserDeptEntity> queryByUser(Long userId);
}

