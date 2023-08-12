package com.atom.minio.controller;

import com.atom.minio.utils.QRCodeUtil;
import com.atom.minio.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Atom
 */
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    /**
     * 根据 content 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    @GetMapping("/getQRCodeBase64")
    public R getQRCode(@RequestParam("content") String content,
                       @RequestParam(value = "logoUrl", required = false) String logoUrl,
                       @RequestParam(value = "width", required = false) Integer width,
                       @RequestParam(value = "height", required = false) Integer height) {
        return R.ok(QRCodeUtil.getBase64QRCode(content, logoUrl));
    }

    /**
     * 华为技术文档：扫描分享文档
     * GET :
     * http://localhost:8080/qrcode/getQRCode?content=https://support.huaweicloud.com/sdkreference-mrs/mrs_11_000001.html
     *
     * 根据 content 生成二维码
     */
    @GetMapping(value = "/getQRCode")
    public void getQRCode(HttpServletResponse response,
                          @RequestParam("content") String content,
                          @RequestParam(value = "logoUrl", required = false) String logoUrl) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            QRCodeUtil.getQRCode(content, logoUrl, stream);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }


}
