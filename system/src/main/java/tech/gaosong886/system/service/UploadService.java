package tech.gaosong886.system.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import tech.gaosong886.system.model.vo.FileUploadVO;

@Service
public class UploadService {
    private static final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png");

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.base-url}")
    private String uploadBaseUrl;

    /**
     * 上传文件
     * @param file
     * @return FileUploadVO
     */
    @SuppressWarnings("null")
    public FileUploadVO upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please select one file for upload.");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported file type.");
        }

        try {
            // 创建唯一文件名
            String fileName = createUniqueFileName(file.getOriginalFilename());

            // 创建上传目录（如果不存在）
            Files.createDirectories(Paths.get(uploadDir));

            Path filePath = Paths.get(uploadDir + "/" + fileName);
            file.transferTo(filePath.toFile());

            return new FileUploadVO(fileName, "done", uploadBaseUrl + "/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Upload failed.");
        }
    }

    /**
     * 生成文件名
     * @param originalFilename 原文件名
     * @return 生成的新文件名
     */
    private String createUniqueFileName(String originalFilename) {
        String fileExtension = "";
        if (originalFilename != null && originalFilename.lastIndexOf(".") != -1 &&
                originalFilename.lastIndexOf(".") != 0) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniquePart = UUID.randomUUID().toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return timestamp + "-" + uniquePart + fileExtension;
    }
}
