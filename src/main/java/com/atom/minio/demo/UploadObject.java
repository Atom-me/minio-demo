package com.atom.minio.demo;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class UploadObject {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://10.16.118.230:9000")
                            .credentials("admin", "12345678")
                            .build();

            // Make 'asiatrip' bucket if not exist.
            String bucketName = "asiatrip";
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            // Upload '/Users/atom/testDir/nginx_docker_test/index.html' as object name 'atom_index.html' to bucket 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("asiatrip")
                            .object("atom_index.html")
                            .filename("/Users/atom/testDir/nginx_docker_test/index.html")
                            .build());
            System.out.println(
                    "'/Users/atom/testDir/nginx_docker_test/index.html' is successfully uploaded as "
                            + "object 'atom_index.html' to bucket 'asiatrip'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}
