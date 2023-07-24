package com.atom.minio.service;

import com.atom.minio.dto.VirusScanResult;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Atom
 */
public interface ClamAVService {

    /**
     * Run PING command to clamd to test it is responding.
     *
     * @return
     */
    boolean ping();

    /**
     * connect to ClamAV on ip:3310 using TCP and send file data in the input stream format
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    VirusScanResult scanStream(InputStream inputStream) throws IOException;
}
