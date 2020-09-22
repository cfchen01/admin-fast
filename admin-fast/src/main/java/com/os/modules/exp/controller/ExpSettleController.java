package com.os.modules.exp.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.os.modules.exp.entity.ExpSettleEntity;
import com.os.modules.exp.service.ExpSettleService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 结算方式
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/expsettle")
public class ExpSettleController {
    @Autowired
    private ExpSettleService expSettleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expsettle:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expSettleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expsettle:info")
    public R info(@PathVariable("id") Integer id){
		ExpSettleEntity expSettle = expSettleService.getById(id);

        return R.ok().put("expSettle", expSettle);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expsettle:save")
    public R save(@RequestBody ExpSettleEntity expSettle){
        expSettle.setCreateTime(new Date());
		expSettleService.save(expSettle);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expsettle:update")
    public R update(@RequestBody ExpSettleEntity expSettle){
		expSettleService.updateById(expSettle);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expsettle:delete")
    public R delete(@RequestBody Integer[] ids){
		expSettleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
