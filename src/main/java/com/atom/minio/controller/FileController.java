package com.atom.minio.controller;

import com.atom.minio.dto.FileInfoResult;
import com.atom.minio.entity.FileInfo;
import com.atom.minio.entity.QrCodeConfig;
import com.atom.minio.entity.QrCodeInfo;
import com.atom.minio.service.FileQrCodeRelationService;
import com.atom.minio.service.FileService;
import com.atom.minio.service.QrCodeConfigService;
import com.atom.minio.service.QrCodeInfoService;
import com.atom.minio.utils.QRCodeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Atom
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;
    @Resource
    private FileQrCodeRelationService fileQrCodeRelationService;

    @Resource
    private QrCodeInfoService qrCodeInfoService;

    @Resource
    private QrCodeConfigService qrCodeConfigService;


    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file111") MultipartFile file) throws Exception {

        fileService.saveFileInfo(file);

        return ResponseEntity.ok("File uploaded and info saved successfully");
    }


    /**
     * 文件列表
     *
     * @return
     */
    @GetMapping("/listObjects")
    public ResponseEntity<List<FileInfoResult>> list() {
        List<FileInfo> fileInfoList = fileService.getAllFiles();

        List<FileInfoResult> fileInfoResultList = fileInfoList.stream()
                .map(this::convertToFileInfoResult)
                .collect(Collectors.toList());

        return ResponseEntity.ok(fileInfoResultList);
    }


    /**
     * 为文件生成二维码，分享使用
     *
     * @param fileId
     * @return
     */
    @GetMapping("/generateQRCodeBase64")
    public String generateQRCodeBase64(@RequestParam("fileId") Long fileId) {
        System.err.println(fileId);

        FileInfo fileInfo = fileService.getFileById(fileId);
        if (Objects.isNull(fileInfo)) {
            // todo
            throw new RuntimeException("fileInfo not exists.");
        }

        // 1. 为每个文件生成一个token，用于关联文件和二维码设置信息（qrCodeData）
        String token = RandomStringUtils.random(12, true, true);

        // 2. token 随URL写入二维码中
        String scanUrl = "http://fileshare.v4.idcfengye.com/share/scan?token=";
        String content = scanUrl + token;

        String base64QRCode = QRCodeUtil.getBase64QRCode(content, null);
        // 保存二维码信息，关联文件
        QrCodeInfo qrCodeInfo = new QrCodeInfo();
        qrCodeInfo.setToken(token);
        qrCodeInfo.setFileId(fileInfo.getId());
        qrCodeInfo.setQrCodeImage(base64QRCode);
        qrCodeInfo.setGeneratedAt(LocalDateTime.now());
        LocalDateTime expireAt = LocalDateTime.of(2023, 8, 16, 23, 59, 59);
        qrCodeInfo.setExpiresAt(expireAt);
        Long qrCodeId = qrCodeInfoService.insertQrCodeInfo(qrCodeInfo);


//        FileQrCodeRelation fileQrCodeRelation = new FileQrCodeRelation();
//        fileQrCodeRelation.setFileId(fileInfo.getId());
//        fileQrCodeRelation.setQrCodeId(qrCodeId);
//        fileQrCodeRelation.setToken(token);
//        fileQrCodeRelation.setCreatedAt(LocalDateTime.now());
//        fileQrCodeRelationService.insertFileQrCodeRelation(fileQrCodeRelation);


        // 用户对各个文件二维码做单独设置（是否需要分享码，是否有地理位置限制，是否有扫码时间段限制）
        LocalDateTime startTime = LocalDateTime.of(2023, 8, 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 8, 16, 23, 59, 59);
        QrCodeConfig qrCodeConfig = new QrCodeConfig();
        qrCodeConfig.setQrcodeId(qrCodeId);
        qrCodeConfig.setShareCode("3333");
        qrCodeConfig.setScanStartTime(startTime);
        qrCodeConfig.setScanEndTime(endTime);
        qrCodeConfig.setCreatedAt(LocalDateTime.now());
        qrCodeConfig.setUpdatedAt(LocalDateTime.now());

        qrCodeConfigService.insertQrCodeConfig(qrCodeConfig);

        return base64QRCode;
    }

    private FileInfoResult convertToFileInfoResult(FileInfo fileInfo) {
        FileInfoResult fileInfoResult = new FileInfoResult();
        fileInfoResult.setId(fileInfo.getId());
        fileInfoResult.setFileName(fileInfo.getFileName());
        fileInfoResult.setFileUrl(fileInfo.getFileUrl());
        fileInfoResult.setFileSize(fileInfo.getFileSize());
        fileInfoResult.setLastModified(fileInfo.getLastModified());
        return fileInfoResult;
    }
}