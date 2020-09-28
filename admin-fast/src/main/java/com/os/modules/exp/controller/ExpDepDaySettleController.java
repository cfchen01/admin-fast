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

import com.os.modules.exp.entity.ExpDepDaySettleEntity;
import com.os.modules.exp.service.ExpDepDaySettleService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;



/**
 * 网点日结报表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/expdepdaysettle")
public class ExpDepDaySettleController {
    @Autowired
    private ExpDepDaySettleService expDepDaySettleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expdepdaysettle:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expDepDaySettleService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expdepdaysettle:info")
    public R info(@PathVariable("id") Integer id){
		ExpDepDaySettleEntity expDepDaySettle = expDepDaySettleService.getById(id);

        return R.ok().put("expDepDaySettle", expDepDaySettle);
    }

    /**
     * 信息
     */
    @RequestMapping("/settle")
//    @RequiresPermissions("exp:expdepdaysettle:info")
    public R settle(@RequestBody SettleDto settleDto){
        ExpDepDaySettleEntity expDepDaySettle = expDepDaySettleService.getDeptSettle(settleDto);

        return R.ok().put("expDepDaySettle", expDepDaySettle);
    }

    /**
     * 信息
     */
    @RequestMapping("/settle/list")
//    @RequiresPermissions("exp:expdepdaysettle:info")
    public R settleList(@RequestBody SettleDto settleDto){
        List<ExpDepDaySettleEntity> list = expDepDaySettleService.getSettleList(settleDto);

        return R.ok().put("list", list);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:expdepdaysettle:save")
    public R save(@RequestBody ExpDepDaySettleEntity expDepDaySettle){
		expDepDaySettleService.save(expDepDaySettle);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:expdepdaysettle:update")
    public R update(@RequestBody ExpDepDaySettleEntity expDepDaySettle){
		expDepDaySettleService.updateById(expDepDaySettle);

        return R.ok();
    }

    /**
     * 修改日常支出
     */
    @RequestMapping("/update/comIn")
//    @RequiresPermissions("exp:expcomdaysettle:update")
    public R updateComIn(@RequestParam Map<String, Object> params){
        expDepDaySettleService.updateComMoneyIn(params);

        return R.ok();
    }

    /**
     * 修改结算状态
     */
    @RequestMapping("/update/settle/{id}")
//    @RequiresPermissions("exp:expcomdaysettle:update")
    public R updateSettle(@PathVariable("id") Integer id){
        expDepDaySettleService.updateSettle(id);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:expdepdaysettle:delete")
    public R delete(@RequestBody Integer[] ids){
		expDepDaySettleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
