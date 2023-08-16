//package com.atom.minio.model;
//
//import java.time.LocalDateTime;
//
///**
// * 二维码设置信息
// *
// * @author Atom
// */
//public class QrCodeConfigData {
//    private Long id;
//    private Long qrCodeId;
//
//    private String shareCode;
//    private LocalDateTime scanStartTime;
//    private LocalDateTime scanEndTime;
//
//    public QrCodeConfigData(Long id, Long qrCodeId, String shareCode, LocalDateTime scanStartTime, LocalDateTime scanEndTime) {
//        this.id = id;
//        this.qrCodeId = qrCodeId;
//        this.shareCode = shareCode;
//        this.scanStartTime = scanStartTime;
//        this.scanEndTime = scanEndTime;
//    }
//
//    public QrCodeConfigData() {
//    }
//
//    public String getShareCode() {
//        return shareCode;
//    }
//
//    public void setShareCode(String shareCode) {
//        this.shareCode = shareCode;
//    }
//
//    public LocalDateTime getScanStartTime() {
//        return scanStartTime;
//    }
//
//    public void setScanStartTime(LocalDateTime scanStartTime) {
//        this.scanStartTime = scanStartTime;
//    }
//
//    public LocalDateTime getScanEndTime() {
//        return scanEndTime;
//    }
//
//    public void setScanEndTime(LocalDateTime scanEndTime) {
//        this.scanEndTime = scanEndTime;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getQrCodeId() {
//        return qrCodeId;
//    }
//
//    public void setQrCodeId(Long qrCodeId) {
//        this.qrCodeId = qrCodeId;
//    }
//
//    @Override
//    public String toString() {
//        return "QrCodeConfigData{" +
//                "id=" + id +
//                ", qrCodeId=" + qrCodeId +
//                ", shareCode='" + shareCode + '\'' +
//                ", scanStartTime=" + scanStartTime +
//                ", scanEndTime=" + scanEndTime +
//                '}';
//    }
//}
