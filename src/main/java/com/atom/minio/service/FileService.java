package com.atom.minio.service;

import com.atom.minio.config.MinIOProperties;
import com.atom.minio.entity.FileInfo;
import com.atom.minio.mapper.FileInfoMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Atom
 */
@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;
    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private MinIOProperties minIOProperties;


    public void saveFileInfo(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(file.getOriginalFilename())
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();

        minioClient.putObject(args);

        LOGGER.info("[{}] is successfully uploaded.", file.getOriginalFilename());

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setLastModified(LocalDateTime.now());
        fileInfo.setCreatedAt(LocalDateTime.now());
        // 出于安全考虑，不能存URL，文件bucket不能开放为public，
//        fileInfo.setFileUrl(minIOProperties.getEndpoint() + "/" + minIOProperties.getBucketName() + "/" + file.getOriginalFilename());

        fileInfoMapper.insertFileInfo(fileInfo);
    }

    public List<FileInfo> getAllFiles() {
        return fileInfoMapper.getAllFiles();
    }


    public FileInfo getFileById(Long fileId) {
        return fileInfoMapper.getFileById(fileId);
    }

}
