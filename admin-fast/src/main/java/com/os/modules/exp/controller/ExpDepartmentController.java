package com.os.modules.exp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.os.modules.exp.dto.SettleDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.os.modules.exp.entity.ExpDepartmentEntity;
import com.os.modules.exp.service.ExpDepartmentService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 网点表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/expdepartment")
public class ExpDepartmentController {
    @Autowired
    private ExpDepartmentService expDepartmentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expdepartment:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expDepartmentService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/all")
//    @RequiresPermissions("exp:expdepartment:list")
    public R all(){
        List<ExpDepartmentEntity> list = expDepartmentService.queryAll();

        return R.ok().put("list", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expdepartment:info")
    public R info(@PathVariable("id") Integer id){
		ExpDepartmentEntity expDepartment = expDepartmentService.getById(id);

        return R.ok().put("expDepartment", expDepartment);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expdepartment:save")
    public R save(@RequestBody ExpDepartmentEntity expDepartment){
		expDepartmentService.save(expDepartment);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expdepartment:update")
    public R update(@RequestBody ExpDepartmentEntity expDepartment){
		expDepartmentService.updateById(expDepartment);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expdepartment:delete")
    public R delete(@RequestBody Integer[] ids){
		expDepartmentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
