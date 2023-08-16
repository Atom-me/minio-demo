CREATE TABLE qrcode_info
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '二维码信息ID',
    token         VARCHAR(255)   NOT NULL comment '二维码token',
    file_id       BIGINT         NOT NULL COMMENT '关联的文件ID',
    qr_code_image VARCHAR(10000) NOT NULL COMMENT '二维码图像的Base64编码字符串',
    generated_at  DATETIME       NOT NULL COMMENT '生成时间',
    expires_at    DATETIME       NOT NULL COMMENT '过期时间'
) COMMENT '二维码信息表，存储文件关联的二维码图像信息';
