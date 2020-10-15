package com.os.modules.exp.controller;

import java.util.Arrays;
import java.util.Map;

import com.os.modules.exp.vo.OrderObjVo;
import com.os.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.os.modules.exp.entity.ExpOrderEntity;
import com.os.modules.exp.service.ExpOrderService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 订单表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-21 11:23:42
 */
@RestController
@RequestMapping("exp/exporder")
public class ExpOrderController extends AbstractController {
    @Autowired
    private ExpOrderService expOrderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:exporder:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expOrderService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 导出列表
     */
    @RequestMapping("/download")
//    @RequiresPermissions("exp:exporder:list")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception{
        expOrderService.download(request, response);
    }

    /**
     * 列表
     */
    @RequestMapping("/resume")
//    @RequiresPermissions("exp:exporder:list")
    public R resume(@RequestParam Map<String, Object> params){
        params.put("status", 1);
        params.put("settleCode", "TF");
        Integer value1 = expOrderService.getResume(params);
        params.put("status", 0);
        Integer value2 = expOrderService.getResume(params);

        return R.ok().put("value1", value1).put("value2", value2);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:exporder:info")
    public R info(@PathVariable("id") String id){
		ExpOrderEntity expOrder = expOrderService.getDetailById(id);

        return R.ok().put("expOrder", expOrder);
    }

    /**
     * 信息
     */
    @RequestMapping("/getByCode/{code}")
//    @RequiresPermissions("exp:exporder:info")
    public R getByCode(@PathVariable("code") String code){
        ExpOrderEntity expOrder = expOrderService.getByCode(code);

        return R.ok().put("expOrder", expOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("exp:exporder:save")
    public R save(@RequestBody ExpOrderEntity expOrder){
		expOrderService.saveOrder(expOrder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("exp:exporder:update")
    public R update(@RequestBody ExpOrderEntity expOrder){
		expOrderService.updateOrder(expOrder);

        return R.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/status")
//    @RequiresPermissions("exp:exporder:update")
    public R status(@RequestParam Map<String, Object> params){
        expOrderService.updateStatus(params);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("exp:exporder:delete")
    public R delete(@RequestBody Integer[] ids){
		expOrderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    @RequestMapping("/orderVo")
//    @RequiresPermissions("exp:exporder:delete")
    public R getOrderObjVo(){
        OrderObjVo orderObjVo = expOrderService.getOrderObjVo();

        return R.ok().put("orderObjVo", orderObjVo);
    }

}
