package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpUserMoneyEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 业务员提货单费用表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
public interface ExpUserMoneyService extends IService<ExpUserMoneyEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改业务员已付金额
     * @return
     */
    Boolean updateUserMoney(ExpUserMoneyEntity expUserMoney);

    List<ExpUserMoneyEntity> getUserMoneyList(Integer daySettleId);
}

