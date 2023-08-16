

CREATE TABLE files
(
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '文件ID',
    file_name     VARCHAR(255) NOT NULL COMMENT '文件名称',
    file_size     BIGINT       NOT NULL COMMENT '文件大小（字节）',
    file_url      VARCHAR(255) default NULL COMMENT '文件URL',
    last_modified DATETIME     NOT NULL COMMENT '最后修改时间',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '文件信息表，用于存储上传的文件基本信息';
