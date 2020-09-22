package com.os.common.utils;

import com.os.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;

public class UserUtils {

    public static SysUserEntity getUser() {
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }
}
