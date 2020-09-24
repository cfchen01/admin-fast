package com.os.modules.exp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.os.common.utils.PageUtils;
import com.os.modules.exp.entity.ExpFileEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 文件信息表
 *
 * @author ccf
 * @email sunlightcs@gmail.com
 * @date 2020-09-23 09:32:44
 */
public interface ExpFileService extends IService<ExpFileEntity> {

    PageUtils queryPage(Map<String, Object> params);

    ExpFileEntity uploadImage(MultipartFile multipartFile);

    void showImg(HttpServletResponse response, String path);
}

