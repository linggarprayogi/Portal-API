package com.glovis.portal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static String saveFile(String fileName, MultipartFile multipartFile, String uploadPath) throws IOException {
        Path path = Paths.get(uploadPath);
          
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
 
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {       
            throw new IOException("Could not save file: " + fileName, ioe);
        }
        
        return uploadPath + "/" +fileName;
    }
}
