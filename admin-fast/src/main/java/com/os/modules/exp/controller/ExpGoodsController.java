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

import com.os.modules.exp.entity.ExpGoodsEntity;
import com.os.modules.exp.service.ExpGoodsService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 货物表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/expgoods")
public class ExpGoodsController {
    @Autowired
    private ExpGoodsService expGoodsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expgoods:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expGoodsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expgoods:info")
    public R info(@PathVariable("id") Integer id){
		ExpGoodsEntity expGoods = expGoodsService.getById(id);

        return R.ok().put("expGoods", expGoods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expgoods:save")
    public R save(@RequestBody ExpGoodsEntity expGoods){
        expGoods.setCreateTime(new Date());
		expGoodsService.save(expGoods);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expgoods:update")
    public R update(@RequestBody ExpGoodsEntity expGoods){
		expGoodsService.updateById(expGoods);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expgoods:delete")
    public R delete(@RequestBody Integer[] ids){
		expGoodsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
