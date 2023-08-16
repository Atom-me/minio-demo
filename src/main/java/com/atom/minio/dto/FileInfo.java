package com.atom.minio.dto;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
public class FileInfo {

    private String fileName;
    private Long fileSize;

    private LocalDateTime lastModified;

    private String fileUrl;

    public FileInfo(String fileName, Long fileSize, LocalDateTime lastModified, String fileUrl) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
        this.fileUrl = fileUrl;
    }

    public FileInfo() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", lastModified=" + lastModified +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
