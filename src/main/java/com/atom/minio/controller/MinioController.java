package com.atom.minio.controller;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.atom.minio.constant.Constants.DOWNLOAD_DIR;

/**
 * @author Atom
 */
@RestController
@RequestMapping("/")
public class MinioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioController.class);
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;


    /**
     * curl --location 'localhost:8080/listObjects'
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listObjects")
    public List<Object> list() throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs
                .builder()
                .bucket(bucketName)
                .recursive(true)
                .build());

        List<Object> items = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            LOGGER.info("桶：[{}],  对象名称：[{}], 对象是否是目录：[{}], 对象大小(字节)[{}] ", bucketName, item.objectName(), item.isDir(), item.size());
            items.add(item.objectName());
        }
        return items;
    }


    /**
     * @param object
     * @throws Exception
     */
    @GetMapping("/{object}")
    public void getObject(@PathVariable("object") String object) throws Exception {

//        DownloadObjectArgs downloadObjectArgs = DownloadObjectArgs.builder()
//                .bucket(bucketName)
//                .object(object)
//                .filename(DOWNLOAD_DIR + object)
////                .overwrite(true)
//                .build();

        GetObjectArgs getObjectArgs = GetObjectArgs
                .builder()
                .bucket(bucketName)
                .object(object)
                .build();
        GetObjectResponse getObjectResponse = minioClient.getObject(getObjectArgs);
        LOGGER.info("[{}] is successfully downloaded to [{}]", object, DOWNLOAD_DIR + object);

    }


    /**
     * curl --location 'localhost:8080' \
     * --header 'Content-Type: multipart/form-data;' \
     * --form 'file111=@"/Users/atom/Desktop/temp00.png"'
     *
     * @param file
     * @throws Exception
     */
    @PostMapping
    public void addAttachment(@RequestParam("file111") MultipartFile file) throws Exception {

        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(file.getOriginalFilename())
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();

        minioClient.putObject(args);

    }


}
