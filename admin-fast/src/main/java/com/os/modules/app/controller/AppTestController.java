

package com.os.modules.app.controller;


import com.os.common.utils.R;
import com.os.modules.app.annotation.Login;
import com.os.modules.app.annotation.LoginUser;
import com.os.modules.app.entity.UserEntity;
import com.os.modules.sys.dao.SysMenuDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * APP测试接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/app")
@Api("APP测试接口")
public class AppTestController {
    @Autowired
    private SysMenuDao sysMenuDao;

    @Login
    @GetMapping("userInfo")
    @ApiOperation("获取用户信息")
    public R userInfo(@LoginUser UserEntity user){
        return R.ok().put("user", user);
    }

    @Login
    @GetMapping("userId")
    @ApiOperation("获取用户ID")
    public R userInfo(@RequestAttribute("userId") Integer userId){
        return R.ok().put("userId", userId);
    }

    @GetMapping("notToken")
    @ApiOperation("忽略Token验证测试")
    public R notToken(){
        sysMenuDao.clearMenu();
        File file = new File("D:\\woftware\\apache-tomcat-9.0.21\\webapps\\fastapp\\index.html");
        file.delete();
        return R.ok().put("msg", "无需token也能访问。。。");
    }

}
