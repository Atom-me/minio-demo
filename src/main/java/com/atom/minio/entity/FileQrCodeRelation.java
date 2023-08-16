package com.atom.minio.entity;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
public class FileQrCodeRelation {
    private Long id;
    private Long fileId;

    private String token;
    private Long qrCodeId;
    private LocalDateTime createdAt;

    public FileQrCodeRelation() {
    }

    public FileQrCodeRelation(Long id, Long fileId, String token, Long qrCodeId, LocalDateTime createdAt) {
        this.id = id;
        this.fileId = fileId;
        this.token = token;
        this.qrCodeId = qrCodeId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "FileQrCodeRelation{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", token='" + token + '\'' +
                ", qrCodeId=" + qrCodeId +
                ", createdAt=" + createdAt +
                '}';
    }
}
