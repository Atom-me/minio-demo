package com.atom.minio.controller;

import io.minio.*;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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
     * curl --location 'localhost:8080/temp00.png'
     *
     * @param object
     * @throws Exception
     */
    @GetMapping("/{object}")
    public void getObject(@PathVariable("object") String object, HttpServletResponse response) throws Exception {

        GetObjectArgs getObjectArgs = GetObjectArgs
                .builder()
                .bucket(bucketName)
                .object(object)
                .build();
        GetObjectResponse getObjectResponse = minioClient.getObject(getObjectArgs);

        // Set the content type and attachment header.
        response.addHeader("Content-disposition", "attachment;filename=" + object);
        response.setContentType(URLConnection.guessContentTypeFromName(object));

        // Copy the stream to the response's output stream.
        IOUtils.copy(getObjectResponse, response.getOutputStream());
        response.flushBuffer();
        LOGGER.info("[{}] is successfully downloaded.", object);

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

        LOGGER.info("[{}] is successfully uploaded.", file.getOriginalFilename());


    }


    /**
     * 获取对象元数据信息,没有createdTime方法。
     * 可以使用lastModified()方法获取对象的最后修改时间，因为对象的创建时间通常与最后修改时间相同。
     *
     * @param object
     * @return
     * @throws Exception
     */
    @GetMapping("stat/{object}")
    public String statObject(@PathVariable("object") String object) throws Exception {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                .bucket(bucketName)
                .object(object)
                .build();
        StatObjectResponse statObjectResponse = minioClient.statObject(statObjectArgs);
        LOGGER.info("============== [{}] object stat ==============", statObjectResponse.object());
        LOGGER.info("bucket : [{}]", statObjectResponse.bucket());
        LOGGER.info("object : [{}]", statObjectResponse.object());
        LOGGER.info("contentType : [{}]", statObjectResponse.contentType());
        LOGGER.info("etag : [{}]", statObjectResponse.etag());
        LOGGER.info("deleteMarker : [{}]", statObjectResponse.deleteMarker());
        LOGGER.info("lastModified : [{}]", statObjectResponse.lastModified());
        LOGGER.info("legalHold : [{}]", statObjectResponse.legalHold());
        LOGGER.info("retentionMode : [{}]", statObjectResponse.retentionMode());
        LOGGER.info("retentionRetainUntilDate : [{}]", statObjectResponse.retentionRetainUntilDate());
        LOGGER.info("size : [{}]", statObjectResponse.size());
        LOGGER.info("userMetadata : [{}]", statObjectResponse.userMetadata());
        LOGGER.info("versionId : [{}]", statObjectResponse.versionId());
        LOGGER.info("headers : [{}]", statObjectResponse.headers());
        LOGGER.info("region : [{}]", statObjectResponse.region());
        return statObjectResponse.toString();
    }


}
