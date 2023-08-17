package com.atom.minio.controller;

import com.atom.minio.config.MinIOProperties;
import com.atom.minio.entity.FileInfo;
import com.atom.minio.entity.QrCodeConfig;
import com.atom.minio.entity.QrCodeInfo;
import com.atom.minio.service.FileService;
import com.atom.minio.service.QrCodeConfigService;
import com.atom.minio.service.QrCodeInfoService;
import com.atom.minio.utils.DateUtil;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * 二维码分享文件
 *
 * @author Atom
 */
@Controller
@RequestMapping("/share")
public class ShareFileController {

    @Resource
    private QrCodeInfoService qrCodeInfoService;

    @Resource
    private QrCodeConfigService qrCodeConfigService;

    @Resource
    private FileService fileService;

    @Resource
    private MinIOProperties minIOProperties;

    @Resource
    private MinioClient minioClient;
    @Resource
    private ResourceLoader resourceLoader;


    /**
     * 二维码内容 写入本接口信息 http://fileshare.v4.idcfengye.com/share/scan?token=1242qwerq3
     * 一个文件多个二维码
     * 一个二维码一个token
     * 一个token对应一个二维码配置
     *
     * @param token
     * @return
     */
    @GetMapping("/scan")
    public String scanQRCode(@RequestParam(value = "token", required = false) String token) {
        // parse token
        QrCodeInfo qrCodeInfo = qrCodeInfoService.getQrCodeInfoByToken(token);
        LocalDateTime expiresAt = qrCodeInfo.getExpiresAt();
        if (LocalDateTime.now().isAfter(expiresAt)) {
            return "redirect:/expired.html?expiredAt=" + DateUtil.format(expiresAt);
        }

        // todo
        // 1. 支持设置查看地域，可设置指定地域才可查看二维码内容，不在设置地域内扫码无法查看内容。适用于签到登记、现场报名、巡逻检查等应用场景

        // 2. 支持设置每天查看时段，可设置每天指定时段才可查看二维码内容，不在设置时段内无法查看内容。适用于教育培训、活动推广等应用场景

        // 3. 活码支持加密功能，可设置密码，扫码后输入密码显示二维码内容。适用于文件传阅、教学资料等应用场景
        QrCodeConfig qrCodeConfig = qrCodeConfigService.getQrCodeConfigByQrCodeId(qrCodeInfo.getId());
        if (StringUtils.isNotBlank(qrCodeConfig.getShareCode())) {
            return "redirect:/encryption.html?token=" + token;
        }
        return "redirect:/null.html";

    }


    @GetMapping("/code")
    @ResponseBody
    public ResponseEntity<String> accessQRCode(@RequestParam("shareCode") String shareCode, @RequestParam("token") String token, HttpServletResponse httpServletResponse) throws Exception {
        System.err.println(shareCode);
        System.err.println(token);
        QrCodeInfo qrCodeInfo = qrCodeInfoService.getQrCodeInfoByToken(token);

        QrCodeConfig qrCodeConfig = qrCodeConfigService.getQrCodeConfigByQrCodeId(qrCodeInfo.getId());
        if (!shareCode.equalsIgnoreCase(qrCodeConfig.getShareCode())) {

            return ResponseEntity.ok().body(getPasswordErrorPageContent());
        }

        Long fileId = qrCodeInfo.getFileId();
        FileInfo fileInfo = fileService.getFileById(fileId);
        String fileName = fileInfo.getFileName();

        // todo 根据fileName 去minio拿文件的stream信息，下载
/*

        String fileUrl = getFileUrlFromCode(shareCode);
        byte[] fileBytesFromOSS = getFileBytesFromOSS(fileUrl);

*/


        GetObjectArgs getObjectArgs = GetObjectArgs
                .builder()
                .bucket(minIOProperties.getBucketName())
                .object(fileName)
                .build();
        GetObjectResponse getObjectResponse = minioClient.getObject(getObjectArgs);

//        httpServletResponse.setContentType("application/pdf");
        String contentType = MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        httpServletResponse.setContentType(contentType);
        ServletOutputStream out = httpServletResponse.getOutputStream();
        IOUtils.copy(getObjectResponse, out);

//        out.write(fileBytesFromOSS);
        out.flush();
        out.close();
        return ResponseEntity.ok().body("ok ");
    }

    private String getFileUrlFromCode(String code) {
        return "https://cdp111.oss-cn-zhangjiakou.aliyuncs.com/upload/20230721/d1d22df128c4ea9fb509aac8c1b5098d.pdf";
    }

    private byte[] getFileBytesFromOSS(String fileUrl) {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(requestFactory);

        RequestCallback requestCallback = request -> {
            if (request instanceof HttpURLConnection) {
                ((HttpURLConnection) request).setRequestMethod(HttpMethod.GET.name());
            }
        };

        ResponseExtractor<byte[]> responseExtractor = response -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                try (InputStream inputStream = response.getBody()) {
                    return StreamUtils.copyToByteArray(inputStream);
                }
            } else {
                throw new IOException("Failed to retrieve file from OSS. Response code: " + response.getStatusCode());
            }
        };

        byte[] fileBytes = restTemplate.execute(fileUrl, HttpMethod.GET, requestCallback, responseExtractor);
        return fileBytes;
    }

    private String getPasswordErrorPageContent() throws IOException {
        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:/static/password_error.html");

        StringBuilder contentBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(resource.getInputStream(), StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()) {
                contentBuilder.append(scanner.nextLine());
            }
        }
        return contentBuilder.toString();
    }
}






