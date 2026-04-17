package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @PostMapping
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("请选择要上传的文件");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = ".jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String fileName = UUID.randomUUID().toString() + extension;

            File dateDir = new File(uploadPath + File.separator + datePath.replace("/", File.separator));
            if (!dateDir.exists()) {
                dateDir.mkdirs();
            }

            Path destPath = Paths.get(dateDir.getAbsolutePath(), fileName);
            Files.copy(file.getInputStream(), destPath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + datePath + "/" + fileName;

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("fileName", fileName);

            return Result.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}
