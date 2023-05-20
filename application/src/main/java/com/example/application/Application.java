package com.example.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/file/info")
    public FileResponse getFileInfo(@RequestParam String filePaths) {
        MultipartFile multipartFile = generateMultiPartFile(filePaths);
        return FileResponse.builder().fileName(multipartFile.getName())
                .fileType(multipartFile.getContentType())
                .size(multipartFile.getSize()).build();
    }


    @SneakyThrows
    private MultipartFile generateMultiPartFile(String filePath) {
        File file = new File(filePath);
        final DiskFileItem diskFileItem = new DiskFileItem("fileData",
                "text/plain", true,
                file.getName(), 100000000, file.getParentFile());

        try (InputStream input = new FileInputStream(filePath);
             OutputStream os = diskFileItem.getOutputStream()) {
            IOUtils.copy(input, os);
        }
        return new CommonsMultipartFile(diskFileItem);
    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class FileResponse {
        long size;
        String fileName;
        String fileType;
        String content;
    }

}
