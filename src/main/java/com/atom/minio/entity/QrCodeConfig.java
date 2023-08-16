package com.atom.minio.entity;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
public class QrCodeConfig {
    private Long id;
    private Long qrcodeId;
    private String shareCode;
    private LocalDateTime scanStartTime;
    private LocalDateTime scanEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QrCodeConfig(Long id, Long qrcodeId, String shareCode, LocalDateTime scanStartTime, LocalDateTime scanEndTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.qrcodeId = qrcodeId;
        this.shareCode = shareCode;
        this.scanStartTime = scanStartTime;
        this.scanEndTime = scanEndTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public QrCodeConfig() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(Long qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public LocalDateTime getScanStartTime() {
        return scanStartTime;
    }

    public void setScanStartTime(LocalDateTime scanStartTime) {
        this.scanStartTime = scanStartTime;
    }

    public LocalDateTime getScanEndTime() {
        return scanEndTime;
    }

    public void setScanEndTime(LocalDateTime scanEndTime) {
        this.scanEndTime = scanEndTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "QrCodeConfig{" +
                "id=" + id +
                ", qrcodeId=" + qrcodeId +
                ", shareCode='" + shareCode + '\'' +
                ", scanStartTime=" + scanStartTime +
                ", scanEndTime=" + scanEndTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
