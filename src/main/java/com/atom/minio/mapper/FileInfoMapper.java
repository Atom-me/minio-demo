package com.atom.minio.mapper;

import com.atom.minio.entity.FileInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Atom
 */
public interface FileInfoMapper {

    void insertFileInfo(FileInfo fileInfo);


    List<FileInfo> getAllFiles();

    FileInfo getFileById(@Param("fileId") Long fileId);


}
