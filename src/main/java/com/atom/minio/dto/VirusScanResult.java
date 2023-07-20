package com.atom.minio.dto;

import com.atom.minio.enums.VirusScanStatus;
import org.springframework.util.StringUtils;

/**
 * @author Atom
 */
public class VirusScanResult {

    private VirusScanStatus status;
    private String result;
    private String signature;

    public VirusScanResult() {
    }

    public VirusScanResult(VirusScanStatus status, String result) {
        this.status = status;
        this.result = result;
    }

    public VirusScanResult(VirusScanStatus status, String result, String signature) {
        this.status = status;
        this.result = result;
        this.signature = signature;
    }


    public VirusScanStatus getStatus() {
        return status;
    }

    public void setStatus(VirusScanStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ");
        sb.append(getStatus());
        sb.append(System.lineSeparator());

        if (StringUtils.hasText(getResult())) {
            sb.append("Result: ");
            sb.append(getResult());
            sb.append(System.lineSeparator());
        }

        if (StringUtils.hasText(getSignature())) {
            sb.append("Signature: ");
            sb.append(getSignature());
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
