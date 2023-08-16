package com.atom.minio.entity;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
public class FileInfo {

    private Long id;
    private String fileName;
    private Long fileSize;
    private String fileUrl;
    private LocalDateTime lastModified;
    private LocalDateTime createdAt;

    public FileInfo() {
    }

    public FileInfo(Long id, String fileName, Long fileSize, String fileUrl, LocalDateTime lastModified, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
        this.lastModified = lastModified;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileUrl='" + fileUrl + '\'' +
                ", lastModified=" + lastModified +
                ", createdAt=" + createdAt +
                '}';
    }
}
