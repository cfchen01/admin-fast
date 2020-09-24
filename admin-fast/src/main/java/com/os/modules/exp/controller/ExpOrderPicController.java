package com.os.modules.exp.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.os.modules.exp.entity.ExpOrderPicEntity;
import com.os.modules.exp.service.ExpOrderPicService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 订单图片表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
@RestController
@RequestMapping("exp/exporderpic")
public class ExpOrderPicController {
    @Autowired
    private ExpOrderPicService expOrderPicService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:exporderpic:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expOrderPicService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:exporderpic:info")
    public R info(@PathVariable("id") Integer id){
		ExpOrderPicEntity expOrderPic = expOrderPicService.getById(id);

        return R.ok().put("expOrderPic", expOrderPic);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:exporderpic:save")
    public R save(@RequestBody ExpOrderPicEntity expOrderPic){
		expOrderPicService.save(expOrderPic);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:exporderpic:update")
    public R update(@RequestBody ExpOrderPicEntity expOrderPic){
		expOrderPicService.updateById(expOrderPic);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:exporderpic:delete")
    public R delete(@RequestBody Integer[] ids){
		expOrderPicService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
