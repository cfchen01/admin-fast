package com.os.modules.exp.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.os.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.os.modules.exp.entity.ExpExpensesDetailEntity;
import com.os.modules.exp.service.ExpExpensesDetailService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 公司支出明细表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/expexpensesdetail")
public class ExpExpensesDetailController extends AbstractController {
    @Autowired
    private ExpExpensesDetailService expExpensesDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expexpensesdetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expExpensesDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expexpensesdetail:info")
    public R info(@PathVariable("id") Integer id){
		ExpExpensesDetailEntity expExpensesDetail = expExpensesDetailService.getById(id);

        return R.ok().put("expExpensesDetail", expExpensesDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expexpensesdetail:save")
    public R save(@RequestBody ExpExpensesDetailEntity expExpensesDetail){
        expExpensesDetail.setCreateTime(new Date());
        expExpensesDetail.setCreateDate(LocalDate.now());
        expExpensesDetail.setUserId(getUserId());
		expExpensesDetailService.save(expExpensesDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expexpensesdetail:update")
    public R update(@RequestBody ExpExpensesDetailEntity expExpensesDetail){
		expExpensesDetailService.updateById(expExpensesDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expexpensesdetail:delete")
    public R delete(@RequestBody Integer[] ids){
		expExpensesDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
