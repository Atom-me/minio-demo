package com.atom.minio.mapper;

import com.atom.minio.entity.QrCodeInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Atom
 */
public interface QrCodeInfoMapper {
    void insertQrCodeInfo(QrCodeInfo qrCodeInfo);

    QrCodeInfo selectByToken(@Param("token") String token);


}
