package com.os.modules.exp.controller;

import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.os.modules.exp.entity.ExpFileEntity;
import com.os.modules.exp.service.ExpFileService;
import com.os.common.utils.PageUtils;
import com.os.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 文件信息表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
@RestController
@RequestMapping("exp/expfile")
public class ExpFileController {
    @Autowired
    private ExpFileService expFileService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("exp:expfile:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = expFileService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("exp:expfile:info")
    public R info(@PathVariable("id") Integer id){
		ExpFileEntity expFile = expFileService.getById(id);

        return R.ok().put("expFile", expFile);
    }

    @RequestMapping("/upload")
    public R uploadOne(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        ExpFileEntity expFile = expFileService.uploadImage(multipartFile);
        return R.ok().put("expFile",expFile);
    }

    @RequestMapping("showImg")
    public void showImg(HttpServletResponse response, @RequestParam(value = "path")String path) {

        expFileService.showImg(response, path);
    }


}
