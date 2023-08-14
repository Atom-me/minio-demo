package com.atom.minio.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Atom
 */
@Slf4j
@UtilityClass
public class QRCodeUtil {

    /**
     * 二维码默认宽度
     */
    private static final Integer DEFAULT_WIDTH = 440;
    /**
     * 二维码默认高度
     */
    private static final Integer DEFAULT_HEIGHT = 440;

    /**
     * LOGO 默认宽度
     */
    private static final Integer DEFAULT_LOGO_WIDTH = 120;
    /**
     * LOGO 默认高度
     */
    private static final Integer DEFAULT_LOGO_HEIGHT = 120;

    /**
     * 二维码图片格式
     */
    private static final String IMAGE_FORMAT = "png";
    private static final String CHARSET = "utf-8";

    /**
     * 二维码与图片的边距
     */
    private static final int MARGIN_SIZE = 2;

    /**
     * FRONT_COLOR：二维码前景色，0x000000 表示黑色
     */
    private static final int FRONT_COLOR = 0xFF000000;

    /**
     * BACKGROUND_COLOR：二维码背景色， 0xFFFFFF 表示白色
     */
    private static final int BACKGROUND_COLOR = 0xFFFFFFFF;

    /**
     * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析
     */
    private static final String BASE64_IMAGE = "data:image/png;base64,%s";


    /**
     * 生成二维码，使用默认尺寸
     *
     * @param content 二维码的内容
     * @return
     */
    public String getBase64QRCode(String content) {
        return getBase64Image(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, null, null, null);
    }

    /**
     * 生成二维码，使用默认尺寸二维码，插入默认尺寸logo
     *
     * @param content 内容
     * @param logoUrl logo地址
     * @return
     */
    public String getBase64QRCode(String content, String logoUrl) {
        return getBase64Image(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, logoUrl, DEFAULT_LOGO_WIDTH, DEFAULT_LOGO_HEIGHT);
    }

    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @return
     */
    public String getBase64QRCode(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        return getBase64Image(content, width, height, logoUrl, logoWidth, logoHeight);
    }

    private String getBase64Image(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bufferedImage = crateQRCode(content, width, height, logoUrl, logoWidth, logoHeight);
        try {
            ImageIO.write(bufferedImage, IMAGE_FORMAT, os);
        } catch (IOException e) {
            log.error("[生成二维码，错误 {}]", e);
        }
        // 转出即可直接使用
//        return String.format(BASE64_IMAGE, Base64.encode(os.toByteArray()));
        return Base64.toBase64String(os.toByteArray());
    }


    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @return
     */
    private BufferedImage crateQRCode(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        HashMap<EncodeHintType, Comparable> hints = new HashMap<>(4);
        // 指定字符编码为utf-8
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 指定二维码的纠错等级为中级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置二维码与图片的边距
        hints.put(EncodeHintType.MARGIN, MARGIN_SIZE);


        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? FRONT_COLOR : BACKGROUND_COLOR);
                }
            }

            if (StringUtils.isNotBlank(logoUrl)) {
                addLogo(qrImage, width, height, logoUrl, logoWidth, logoHeight);
            }

            return qrImage;
        } catch (Exception e) {
            log.error("[生成二维码错误]", e);
            throw new RuntimeException("[生成二维码错误]", e);
        }
    }

    /**
     * 二维码插入logo
     *
     * @param qrImage    二维码
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @throws Exception
     */
    private void addLogo(BufferedImage qrImage, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) throws Exception {
        // logo 源可为 File/InputStream/URL
        URL url = new URL(logoUrl);
        BufferedImage logoImage = ImageIO.read(url);
        // 插入LOGO
        Graphics2D graph = qrImage.createGraphics();
        int x = (width - logoWidth) / 2;
        int y = (height - logoHeight) / 2;
        graph.drawImage(logoImage, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 获取二维码
     *
     * @param content 内容
     * @param output  输出流
     * @throws IOException
     */
    public void getQRCode(String content, OutputStream output) throws IOException {
        BufferedImage image = crateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, null, null, null);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }

    /**
     * 获取二维码
     *
     * @param content 内容
     * @param logoUrl logo资源
     * @param output  输出流
     * @throws Exception
     */
    public void getQRCode(String content, String logoUrl, OutputStream output) throws Exception {
        BufferedImage image = crateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, logoUrl, DEFAULT_LOGO_WIDTH, DEFAULT_LOGO_HEIGHT);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }
}
