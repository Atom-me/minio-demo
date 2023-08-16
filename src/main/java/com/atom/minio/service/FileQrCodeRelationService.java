package com.atom.minio.service;

import com.atom.minio.entity.FileQrCodeRelation;
import com.atom.minio.mapper.FileQrCodeRelationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Atom
 */

@Service
public class FileQrCodeRelationService {
    @Resource
    private FileQrCodeRelationMapper relationMapper;


    public void insertFileQrCodeRelation(FileQrCodeRelation relation) {
        relationMapper.insertFileQrCodeRelation(relation);
    }
}