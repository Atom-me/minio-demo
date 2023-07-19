package com.atom.minio.demo;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class UploadObjectDemo {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://10.1.39.95:9000")
                            .credentials("hL2pqJum98hjDOSu", "2QVNvNuRi0ysJJ3n8Xtj6zBYIQKHJXC9")
                            .build();

            // Make 'asiatrip' bucket if not exist.
            String bucketName = "atom-test";
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                System.out.println("Bucket 'atom-test' already exists.");
            }

            // Upload '/Users/atom/testDir/nginx_docker_test/index.html' as object name 'atom_index.html' to bucket 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("atom-test")
                            .object("hello.txt")
                            .filename("/Users/atom/hello.txt")
                            .build());
            System.out.println(
                    "'/Users/atom/hello.txt' is successfully uploaded as "
                            + "object 'hello.txt' to bucket 'atom-test'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}
