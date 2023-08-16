package com.atom.minio.dto;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
public class FileInfoResult {

    private Long id;
    private String fileName;
    private Long fileSize;

    private LocalDateTime lastModified;

    private String fileUrl;

    public FileInfoResult(Long id, String fileName, Long fileSize, LocalDateTime lastModified, String fileUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
        this.fileUrl = fileUrl;
    }

    public FileInfoResult() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FileInfoResult{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", lastModified=" + lastModified +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
