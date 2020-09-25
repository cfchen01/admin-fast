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

import com.os.modules.exp.entity.ExpUserDeptEntity;
import com.os.modules.exp.service.ExpUserDeptService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 用户网点关系表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-24 19:17:49
 */
@RestController
@RequestMapping("exp/expuserdept")
public class ExpUserDeptController {
    @Autowired
    private ExpUserDeptService expUserDeptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expuserdept:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expUserDeptService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expuserdept:info")
    public R info(@PathVariable("id") Integer id){
		ExpUserDeptEntity expUserDept = expUserDeptService.getById(id);

        return R.ok().put("expUserDept", expUserDept);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expuserdept:save")
    public R save(@RequestBody ExpUserDeptEntity expUserDept){
		expUserDeptService.save(expUserDept);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expuserdept:update")
    public R update(@RequestBody ExpUserDeptEntity expUserDept){
		expUserDeptService.updateById(expUserDept);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expuserdept:delete")
    public R delete(@RequestBody Integer[] ids){
		expUserDeptService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
