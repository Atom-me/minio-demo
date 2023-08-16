

CREATE TABLE qr_code_config
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '二维码设置信息ID',
    qrcode_id         BIGINT       NOT NULL COMMENT '关联的二维码ID',
    share_code      VARCHAR(255) default NULL COMMENT '分享码',
    scan_start_time DATETIME     default NULL COMMENT '扫描开始时间',
    scan_end_time   DATETIME     default NULL COMMENT '扫描结束时间',
    created_at      DATETIME     NOT NULL COMMENT '创建时间',
    updated_at      DATETIME     NOT NULL COMMENT '更新时间',
    unique key uk_qrcode_id(qrcode_id)
) COMMENT '二维码设置信息表，用于存储二维码的设置信息';
