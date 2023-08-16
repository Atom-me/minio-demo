package com.atom.minio.controller;

import com.atom.minio.utils.QRCodeUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @author Atom
 */
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    /**
     * 根据 content 生成二维码
     * <p>
     * 阿里云技术文档：扫码分享
     * <p>
     * GET
     * http://localhost:8080/qrcode/getQRCodeBase64?content=https://help.aliyun.com/zh/ecs/product-overview/what-is-ecs?spm=a2c4g.11174283.0.0.1313300cxGYPBd
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    @GetMapping("/getQRCodeBase64")
    public String getQRCode(@RequestParam("content") String content,
                            @RequestParam(value = "logoUrl", required = false) String logoUrl,
                            @RequestParam(value = "width", required = false) Integer width,
                            @RequestParam(value = "height", required = false) Integer height) {

        return QRCodeUtil.getBase64QRCode(content, logoUrl);
    }

    /**
     * 华为技术文档：扫描分享文档
     * GET :
     * http://localhost:8080/qrcode/getQRCode?content=https://support.huaweicloud.com/sdkreference-mrs/mrs_11_000001.html&logoUrl=https://img.ixintu.com/download/jpg/201912/c38f5b7290a8c06d7b2ef89717f09432.jpg!con
     * <p>
     * 根据 content 生成二维码
     */
    @GetMapping(value = "/getQRCode")
    public void getQRCode(HttpServletResponse response,
                          @RequestParam("content") String content,
                          @RequestParam(value = "logoUrl", required = false) String logoUrl) {
        try (ServletOutputStream stream = response.getOutputStream()) {
            QRCodeUtil.getQRCode(content, logoUrl, stream);
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * http://localhost:8080/qrcode/generate-qr?qrText=atom
     * <p>
     * 直接返回 BufferedImage,
     * 配置BufferedImageHttpMessageConverter
     *
     * @param qrText
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/generate-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage generateQRCodeImage(@RequestParam String qrText) throws Exception {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 250, 250);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}
