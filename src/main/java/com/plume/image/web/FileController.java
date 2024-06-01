package com.plume.image.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class FileController {

    @Value("${files.upload.path}")
    private String uploadDir;

    @GetMapping("/")
    public String index(Model model) throws IOException {
        List<String> uploadedFiles = getUploadedFiles();
        model.addAttribute("uploadedFiles", uploadedFiles);
        return "upload";
    }

    @GetMapping("/preview/random")
    public ResponseEntity<byte[]> previewRandomImage() throws IOException {
        List<Path> imagePaths = Files.list(Paths.get(uploadDir))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        if (imagePaths.isEmpty()) {
            // 如果没有图片可供预览，返回404错误
            return ResponseEntity.notFound().build();
        }

        // 从图片列表中随机选择一张图片
        Random random = new Random();
        Path randomImagePath = imagePaths.get(random.nextInt(imagePaths.size()));

        // 读取图片文件的字节数组
        byte[] imageBytes = Files.readAllBytes(randomImagePath);

        // 设置响应的Content-Type头部
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 或者 MediaType.IMAGE_PNG

        // 返回包含图片字节数组和响应头部的ResponseEntity
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


    @GetMapping("/preview/{fileName}")
    public ResponseEntity<byte[]> previewImage(@PathVariable("fileName") String fileName) throws IOException {
        Path imagePath = Paths.get(uploadDir + fileName);
        byte[] imageBytes = Files.readAllBytes(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }



    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(path, bytes);
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable("fileName") String fileName) throws IOException {
        Path path = Paths.get(uploadDir + fileName);
        Files.deleteIfExists(path);
        return "redirect:/";
    }

    private List<String> getUploadedFiles() throws IOException {
        return Files.list(Paths.get(uploadDir))
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());
    }
}
