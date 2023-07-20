package com.atom.minio.service.impl;

import com.atom.minio.dto.VirusScanResult;
import com.atom.minio.enums.VirusScanStatus;
import com.atom.minio.service.ClamAVService;
import com.atom.minio.service.VirusScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author Atom
 */
@Service
public class VirusScanServiceImpl implements VirusScanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VirusScanServiceImpl.class);

    @Autowired
    private ClamAVService clamAVService;

    @Override
    public VirusScanResult virusScan(MultipartFile file) {
        VirusScanResult scanResult;

        try (InputStream destStream = file.getInputStream()) {
            if (clamAVService.ping()) {
                scanResult = clamAVService.scan(destStream);
            } else {
                LOGGER.error("ClamD did not respond to ping request.");
                scanResult = new VirusScanResult(VirusScanStatus.CONNECTION_FAILED, "ClamAV did not respond to ping request.");
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while scanning file.", e);
            scanResult = new VirusScanResult(VirusScanStatus.ERROR, "An error occurred while scanning file.");
        }

        return scanResult;
    }
}
