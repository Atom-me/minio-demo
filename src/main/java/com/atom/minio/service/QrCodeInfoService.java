package com.atom.minio.service;

import com.atom.minio.entity.QrCodeInfo;
import com.atom.minio.mapper.QrCodeInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Atom
 */

@Service
public class QrCodeInfoService {
    @Resource
    private QrCodeInfoMapper qrCodeInfoMapper;


    public Long insertQrCodeInfo(QrCodeInfo qrCodeInfo) {
        qrCodeInfoMapper.insertQrCodeInfo(qrCodeInfo);
        return qrCodeInfo.getId(); // 返回插入后的ID
    }


    public QrCodeInfo getQrCodeInfoByToken(String token) {
        return qrCodeInfoMapper.selectByToken(token);
    }
}
