package com.atom.minio.controller;

import com.atom.minio.config.MinIOProperties;
import com.atom.minio.dto.FileInfoResult;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.ErrorResponse;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Atom
 */
@RestController
@RequestMapping("/minio")
public class MinioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioController.class);
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Resource
    private MinIOProperties minIOProperties;

    /**
     * curl --location 'localhost:8080/minio/listObjects'
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listObjects")
    public ResponseEntity<List<FileInfoResult>> list() throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs
                .builder()
                .bucket(bucketName)
                .recursive(true)
                .build());

        List<FileInfoResult> items = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            FileInfoResult fileInfoResult = new FileInfoResult();
            fileInfoResult.setFileName(item.objectName());
            fileInfoResult.setFileSize(item.size());
            fileInfoResult.setLastModified(LocalDateTime.from(item.lastModified()));
            fileInfoResult.setFileUrl(minIOProperties.getEndpoint() + "/" + minIOProperties.getBucketName() + "/" + item.objectName());

            LOGGER.info("桶：[{}],  对象名称：[{}], 对象是否是目录：[{}], 对象大小(字节)[{}] ", bucketName, item.objectName(), item.isDir(), item.size());
            items.add(fileInfoResult);
        }
        return ResponseEntity.ok(items);
    }


    /**
     * curl --location 'localhost:8080/minio/temp00.png'
     *
     * @param object
     * @throws Exception
     */
    @GetMapping("/{object}")
    public ResponseEntity<String> downloadObject(@PathVariable("object") String object, HttpServletResponse response) throws Exception {

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
        return ResponseEntity.ok("success");

    }


    /**
     * curl --location 'localhost:8080/minio' \
     * --header 'Content-Type: multipart/form-data;' \
     * --form 'file111=@"/Users/atom/Desktop/temp00.png"'
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadObject(@RequestParam("file111") MultipartFile file) throws Exception {

        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(file.getOriginalFilename())
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();

        minioClient.putObject(args);

        LOGGER.info("[{}] is successfully uploaded.", file.getOriginalFilename());

        return ResponseEntity.ok("success");

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
    public ResponseEntity<String> statObject(@PathVariable("object") String object) throws Exception {
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
        return ResponseEntity.ok(statObjectResponse.toString());
    }


    /**
     * 重命名对象
     *
     * @param srcObject
     * @param destObject
     * @return
     */
    @PutMapping("/rename/{srcObject}/{destObject}")
    public ResponseEntity<String> renameObject(@PathVariable("srcObject") String srcObject, @PathVariable("destObject") String destObject) {

        try {
            // Perform the object renaming by copying the source object to the destination object in the same bucket.
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucketName)
                            .object(destObject)
                            .source(
                                    CopySource.builder()
                                            .bucket(bucketName)
                                            .object(srcObject)
                                            .build())
                            .build());
            LOGGER.info("[{}]/[{}] copied to [{}]/[{}] successfully", bucketName, srcObject, bucketName, destObject);

            // Delete the source object after it has been successfully copied.
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(srcObject)
                    .build());
            LOGGER.info("[{}] renamed to [{}] successfully", srcObject, destObject);
            return ResponseEntity.ok("Rename successfully!!");

        } catch (Exception e) {
            LOGGER.error("An error occurred during renaming:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rename failed~~~");
        }

    }


    /**
     * 删除对象
     *
     * @param object
     * @return
     */
    @DeleteMapping("/remove/{object}")
    public ResponseEntity<String> removeObject(@PathVariable("object") String object) {
        try {
            // 检查对象是否存在
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(object).build());
            // 如果没有跑出异常，说明存在
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(object)
                    .build());
            LOGGER.info("[{}] remove successfully", object);
            return ResponseEntity.ok(object + " removed successfully");
        } catch (ErrorResponseException e) {
            // 捕获ErrorResponseException，提取异常信息
            ErrorResponse errorResponse = e.errorResponse();
            String errorMessage = errorResponse != null ? errorResponse.message() : "Unknown error";
            LOGGER.error("An error occurred during removing object '{}': {}", object, errorMessage);

            // 如果不存在，minio server会返回 404
            if (e.response().code() == HttpStatus.NOT_FOUND.value()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(object + " does not exist in the bucket");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Remove failed: " + errorMessage);
            }
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred during removing object '{}':", object, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Remove failed: Unexpected error");
        }
    }


}
