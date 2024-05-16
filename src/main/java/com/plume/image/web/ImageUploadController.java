package com.plume.image.web;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class ImageUploadController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 获取文件信息
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize() / 1024;

        // 定义一个唯一的文件标识码
        String uuid = IdUtil.fastSimpleUUID();
        // 定义文件存储路径
        // 这里使用uuid作为文件名，防止文件重名
        // 文件存储路径为：E:/test/files/ + uuid + 文件类型
        File uploadFile = new File(fileUploadPath + uuid + StrUtil.DOT + type);

        // 判断目录是否存在
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String md5;
        String url;
        // 上传文件到磁盘
        file.transferTo(uploadFile);


        return "success";

    }
}