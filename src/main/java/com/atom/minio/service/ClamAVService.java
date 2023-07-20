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
     * scan virus
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    VirusScanResult scan(InputStream inputStream) throws IOException;
}
