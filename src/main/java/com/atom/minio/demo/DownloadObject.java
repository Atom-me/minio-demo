package com.atom.minio.demo;

/*
 * MinIO Java SDK for Amazon S3 Compatible Cloud Storage, (C) 2015 MinIO, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DownloadObject {
    /**
     * MinioClient.getObject() example.
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://10.16.118.230:9000")
                            .credentials("admin", "12345678")
                            .build();

            /* Amazon S3: */
            // MinioClient minioClient =
            //     MinioClient.builder()
            //         .endpoint("https://s3.amazonaws.com")
            //         .credentials("YOUR-ACCESSKEY", "YOUR-SECRETACCESSKEY")
            //         .build();

            // Download 'my-objectname' from 'my-bucketname' to 'my-filename'
            String bucketName = "asiatrip";

            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object("atom_index.html")
                            .filename("/Users/atom/test/a.html")
                            .build());
            System.out.println("my-objectname is successfully downloaded to my-filename");


        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }
}