package com.atom.minio.model;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
public class QrCodeData {
    private String shareCode;
    private LocalDateTime scanStartTime;
    private LocalDateTime scanEndTime;

    public QrCodeData(String shareCode, LocalDateTime scanStartTime, LocalDateTime scanEndTime) {
        this.shareCode = shareCode;
        this.scanStartTime = scanStartTime;
        this.scanEndTime = scanEndTime;
    }

    public QrCodeData() {
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

    @Override
    public String toString() {
        return "QrCodeData{" +
                "shareCode='" + shareCode + '\'' +
                ", scanStartTime=" + scanStartTime +
                ", scanEndTime=" + scanEndTime +
                '}';
    }
}
