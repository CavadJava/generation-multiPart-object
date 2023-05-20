package com.example.application.controller;

import com.example.application.dto.FileResponse;
import lombok.SneakyThrows;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
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

@RestController
@RequestMapping("/api/file/info")
public class FileController {

    @GetMapping
    public FileResponse getFileInfo(@RequestParam String filePath) {
        MultipartFile multipartFile = generateMultiPartFile(filePath);
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


}
