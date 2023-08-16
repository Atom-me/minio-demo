package com.atom.minio.service;

import com.atom.minio.entity.QrCodeConfig;
import com.atom.minio.mapper.QrCodeConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Atom
 */

@Service
public class QrCodeConfigService {

    @Resource
    private QrCodeConfigMapper qrCodeConfigMapper;

    public Long insertQrCodeConfig(QrCodeConfig qrCodeConfig) {
        qrCodeConfigMapper.insertQrCodeConfig(qrCodeConfig);
        return qrCodeConfig.getId();
    }


    public QrCodeConfig getQrCodeConfigByQrCodeId(Long qrCodeId) {
        return qrCodeConfigMapper.selectByQrCodeId(qrCodeId);
    }

}
