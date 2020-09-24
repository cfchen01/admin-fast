package com.os.modules.exp.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.exception.RRException;
import com.os.common.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.os.modules.exp.dao.ExpFileDao;
import com.os.modules.exp.entity.ExpFileEntity;
import com.os.modules.exp.service.ExpFileService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


@Service("expFileService")
public class ExpFileServiceImpl extends ServiceImpl<ExpFileDao, ExpFileEntity> implements ExpFileService {

    @Value("${exp.file.path}")
    private String targetPath;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpFileEntity> page = this.page(
                new Query<ExpFileEntity>().getPage(params),
                new LambdaQueryWrapper<ExpFileEntity>()

        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public ExpFileEntity uploadImage(MultipartFile multipartFile) {
        ExpFileEntity expFileEntity = new ExpFileEntity();
        try {
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filePath = targetPath + File.separator + currentDate + File.separator;
            if (!FileUtil.exist(filePath)) {
                FileUtil.mkdir(filePath);
            }
            String fileName = UUID.randomUUID().toString() + "@" + multipartFile.getOriginalFilename();
            String suffix = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            File file1 = FileUtil.writeBytes(multipartFile.getBytes(), filePath + fileName);
            List<Map<String, String>> pathList = new ArrayList<>();
            if (file1.length() > 0) {
                Map<String, String> map = new HashMap<>();
                map.put("fileName", fileName);
                map.put("suffix", suffix);
                map.put("path", filePath);
                pathList.add(map);
            }

            expFileEntity.setPath(currentDate + "/" + fileName);
            expFileEntity.setCreateTime(new Date());
            expFileEntity.setUserId(UserUtils.getUserId());
            expFileEntity.setUrl("/exp/expfile/showImg?path=" + expFileEntity.getPath());

            this.save(expFileEntity);
        } catch (Exception e){
            e.printStackTrace();
            throw new RRException("图片上传失败");
        }

        return expFileEntity;
    }

    @Override
    public void showImg(HttpServletResponse response, String path) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(FileUtil.readBytes(targetPath + path));
            IoUtil.close(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
