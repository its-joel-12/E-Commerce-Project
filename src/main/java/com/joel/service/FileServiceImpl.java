package com.joel.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // get filename of the original / current file
        String originalFileName= file.getOriginalFilename();

        // generate a unique filename
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.'))); // joe.jpeg --> <uuid>.jpeg
        String filePath = path + File.separator + fileName;

        // check if path exists else create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        // upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // returning filename
        return fileName;
    }
}
