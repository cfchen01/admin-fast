package com.os.modules.exp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.os.modules.exp.entity.ExpUserMoneyEntity;
import com.os.modules.exp.service.ExpUserMoneyService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 业务员提货单费用表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/expusermoney")
public class ExpUserMoneyController {
    @Autowired
    private ExpUserMoneyService expUserMoneyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expusermoney:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expUserMoneyService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/all/{daySettleId}")
//    @RequiresPermissions("exp:expusermoney:list")
    public R list(@PathVariable("daySettleId") Integer daySettleId){
        List<ExpUserMoneyEntity> list = expUserMoneyService.getUserMoneyList(daySettleId);

        return R.ok().put("list", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expusermoney:info")
    public R info(@PathVariable("id") Integer id){
		ExpUserMoneyEntity expUserMoney = expUserMoneyService.getById(id);

        return R.ok().put("expUserMoney", expUserMoney);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expusermoney:save")
    public R save(@RequestBody ExpUserMoneyEntity expUserMoney){
		expUserMoneyService.save(expUserMoney);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expusermoney:update")
    public R update(@RequestBody ExpUserMoneyEntity expUserMoney){
		expUserMoneyService.updateById(expUserMoney);

        return R.ok();
    }

    /**
     * 修改用户已交费用
     */
    @RequestMapping("/update/money")
//    @RequiresPermissions("exp:expcomdaysettle:update")
    public R updateMoney(@RequestBody ExpUserMoneyEntity expUserMoney){
        expUserMoneyService.updateUserMoney(expUserMoney);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expusermoney:delete")
    public R delete(@RequestBody Integer[] ids){
		expUserMoneyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
