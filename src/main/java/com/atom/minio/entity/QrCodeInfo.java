package com.atom.minio.entity;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
public class QrCodeInfo {

    private Long id;

    private String token;
    private Long fileId;
    private String qrCodeImage;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;

    public QrCodeInfo(Long id, String token, Long fileId, String qrCodeImage, LocalDateTime generatedAt, LocalDateTime expiresAt) {
        this.id = id;
        this.token = token;
        this.fileId = fileId;
        this.qrCodeImage = qrCodeImage;
        this.generatedAt = generatedAt;
        this.expiresAt = expiresAt;
    }

    public QrCodeInfo() {
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

    public String getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(String qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "QrCodeInfo{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", fileId=" + fileId +
                ", qrCodeImage='" + qrCodeImage + '\'' +
                ", generatedAt=" + generatedAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
