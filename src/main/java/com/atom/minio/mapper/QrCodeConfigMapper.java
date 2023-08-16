package com.atom.minio.mapper;

import com.atom.minio.entity.QrCodeConfig;
import org.apache.ibatis.annotations.Param;

/**
 * @author Atom
 */
public interface QrCodeConfigMapper {

    void insertQrCodeConfig(QrCodeConfig qrCodeConfig);

    QrCodeConfig selectByQrCodeId(@Param("qrcodeId") Long qrcodeId);

}
