package com.plume.image.web;

import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@RestController
@RequestMapping("/file")
public class RandomDownloadController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @GetMapping("/random-image")
    public void downloadRandomImage(HttpServletResponse response) throws IOException {
        // 获取文件夹内所有文件的列表
        File dir = new File(fileUploadPath);
        File[] files = dir.listFiles((d, name) -> {
            // 确保只选择图片文件，这里以常见的图片扩展名为例
            return name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)");
        });

        if (files == null || files.length == 0) {
            // 如果文件夹为空或无符合条件的文件，可进行相应处理，例如返回错误信息
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 随机选择一个图片文件
        Random random = new Random();
        File selectedFile = files[random.nextInt(files.length)];

        // 设置输出格式
        ServletOutputStream outputStream = response.getOutputStream();
        String fileName = selectedFile.getName();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));
        response.setContentType("application/octet-stream");

        // 读取文件字节流并输出
        outputStream.write(FileUtil.readBytes(selectedFile));
        outputStream.flush();
        outputStream.close();
    }
}
