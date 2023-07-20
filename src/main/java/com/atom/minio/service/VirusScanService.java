package com.atom.minio.service;

import com.atom.minio.dto.VirusScanResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Atom
 */
public interface VirusScanService {
    VirusScanResult virusScan(MultipartFile file);


}
