package com.atom.minio.controller;

import com.atom.minio.dto.VirusScanResult;
import com.atom.minio.service.VirusScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * virus scan controller
 * <p>
 * antivirus
 *
 * @author Atom
 */
@Controller
@RequestMapping("/av")
public class VirusScanController {


    @Autowired
    private VirusScanService virusScanService;


    @PostMapping("/scan")
    public ResponseEntity<VirusScanResult> scan(@RequestParam("file") MultipartFile file) {
        VirusScanResult virusScanResult = virusScanService.virusScan(file);
        return ResponseEntity.ok(virusScanResult);
    }
}
