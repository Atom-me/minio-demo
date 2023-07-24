package com.atom.minio.service.impl;

import com.atom.minio.dto.VirusScanResult;
import com.atom.minio.enums.VirusScanStatus;
import com.atom.minio.service.ClamAVService;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class ClamAVServiceImpl implements ClamAVService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClamAVServiceImpl.class);

    private static final int CHUNK_SIZE = 2048;
    private static final String RESPONSE_OK = "stream: OK";
    private static final String PONG = "PONG";
    private static final String PING = "zPING\0";
    private static final String INSTREAM = "zINSTREAM\0";
    private static final String FOUND_SUFFIX = "FOUND";
    private static final String ERROR_SUFFIX = "ERROR";
    private static final String STREAM_PREFIX = "stream:";

    @Value("${clamav.host}")
    private String host;

    @Value("${clamav.port}")
    private int port;

    @Value("${clamav.connectionTimeout}")
    private int connectionTimeout;

    @Value("${clamav.readTimeout}")
    private int readTimeout;

    @Override
    public boolean ping() {
        try {
            String pingResult = processCommand(PING.getBytes()).trim();
            LOGGER.info("pingResult::::::[{}]", pingResult);
            return pingResult.equalsIgnoreCase(PONG);
        } catch (Exception e) {
            LOGGER.error("Error pinging to ClamAV.", e);
            return false;
        }
    }


    @Override
    public VirusScanResult scanStream(InputStream inputStream) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), connectionTimeout);
            socket.setSoTimeout(readTimeout);

            try (OutputStream outStream = new BufferedOutputStream(socket.getOutputStream())) {
                // handshake
                outStream.write(INSTREAM.getBytes(StandardCharsets.UTF_8));
                outStream.flush();

                byte[] buffer = new byte[CHUNK_SIZE];
                try (InputStream inStream = socket.getInputStream()) {
                    int read = inputStream.read(buffer);

                    while (read >= 0) {
                        byte[] chunkSize = ByteBuffer.allocate(4).putInt(read).array();
                        outStream.write(chunkSize);
                        outStream.write(buffer, 0, read);

                        if (inStream.available() > 0) {
                            byte[] reply = IOUtils.toByteArray(inStream);
                            throw new IOException("Reply from server: " + new String(reply, StandardCharsets.UTF_8));
                        }
                        read = inputStream.read(buffer);
                    }

                    outStream.write(new byte[]{0, 0, 0, 0});
                    outStream.flush();
                    String result = new String(IOUtils.toByteArray(inStream)).trim();
                    LOGGER.info("scan result::::[{}]", result);
                    return populateVirusScanResult(result);
                }
            }
        }
    }

    private String processCommand(final byte[] cmd) throws IOException {
        String response = "";

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), connectionTimeout);
            socket.setSoTimeout(readTimeout);

            try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                dos.write(cmd);
                dos.flush();

                InputStream is = socket.getInputStream();

                int read = CHUNK_SIZE;
                byte[] buffer = new byte[CHUNK_SIZE];

                while (read == CHUNK_SIZE) {
                    try {
                        read = is.read(buffer);
                    } catch (IOException e) {
                        LOGGER.error("Error reading result from socket.", e);
                        break;
                    }
                    response = new String(buffer, 0, read);
                }
            }
        }
        LOGGER.info("cmd response:[{}]", response);
        return response;
    }

    private VirusScanResult populateVirusScanResult(final String result) {
        VirusScanResult scanResult = new VirusScanResult();
        scanResult.setStatus(VirusScanStatus.FAILED);
        scanResult.setResult(result);

        if (!StringUtils.hasText(result)) {
            scanResult.setStatus(VirusScanStatus.ERROR);
        } else if (RESPONSE_OK.equals(result)) {
            scanResult.setStatus(VirusScanStatus.PASSED);
        } else if (result.endsWith(FOUND_SUFFIX)) {
            scanResult.setSignature(
                    result.substring(STREAM_PREFIX.length(), result.lastIndexOf(FOUND_SUFFIX) - 1).trim());
        } else if (result.endsWith(ERROR_SUFFIX)) {
            scanResult.setStatus(VirusScanStatus.ERROR);
        }
        return scanResult;
    }
}
