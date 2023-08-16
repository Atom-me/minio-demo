

CREATE TABLE file_qr_code_relation
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联信息ID',
    file_id    BIGINT   NOT NULL COMMENT '关联的文件ID',
    qr_code_id BIGINT   NOT NULL COMMENT '关联的二维码ID',
    token varchar(255)   NOT NULL COMMENT '关联令牌',
    created_at DATETIME NOT NULL COMMENT '创建时间'
) COMMENT '文件与二维码关联信息表，用于存储文件与二维码的关联关系';
