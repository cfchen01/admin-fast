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

import com.os.modules.exp.entity.ExpComDaySettleEntity;
import com.os.modules.exp.service.ExpComDaySettleService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 公司日结报表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/expcomdaysettle")
public class ExpComDaySettleController {
    @Autowired
    private ExpComDaySettleService expComDaySettleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expcomdaysettle:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expComDaySettleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expcomdaysettle:info")
    public R info(@PathVariable("id") Integer id){
		ExpComDaySettleEntity expComDaySettle = expComDaySettleService.getById(id);

        return R.ok().put("expComDaySettle", expComDaySettle);
    }

    /**
     * 信息
     */
    @RequestMapping("/detail")
//    @RequiresPermissions("exp:expcomdaysettle:info")
    public R detail(@RequestParam Map<String, Object> params){
		ExpComDaySettleEntity expComDaySettle = expComDaySettleService.getByDeliverType(params);

		if (expComDaySettle == null) {
		    return R.error(403, "当前时间尚未结算");
        }
        return R.ok().put("expComDaySettle", expComDaySettle);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expcomdaysettle:save")
    public R save(@RequestBody ExpComDaySettleEntity expComDaySettle){
		expComDaySettleService.save(expComDaySettle);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expcomdaysettle:update")
    public R update(@RequestBody ExpComDaySettleEntity expComDaySettle){
		expComDaySettleService.updateById(expComDaySettle);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expcomdaysettle:delete")
    public R delete(@RequestBody Integer[] ids){
		expComDaySettleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
