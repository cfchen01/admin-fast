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

import com.os.modules.exp.entity.ExpPackingEntity;
import com.os.modules.exp.service.ExpPackingService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 包装表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/exppacking")
public class ExpPackingController {
    @Autowired
    private ExpPackingService expPackingService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:exppacking:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expPackingService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:exppacking:info")
    public R info(@PathVariable("id") Integer id){
		ExpPackingEntity expPacking = expPackingService.getById(id);

        return R.ok().put("expPacking", expPacking);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:exppacking:save")
    public R save(@RequestBody ExpPackingEntity expPacking){
        expPacking.setCreateTime(new Date());
		expPackingService.save(expPacking);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:exppacking:update")
    public R update(@RequestBody ExpPackingEntity expPacking){
		expPackingService.updateById(expPacking);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:exppacking:delete")
    public R delete(@RequestBody Integer[] ids){
		expPackingService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
