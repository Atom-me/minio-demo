## minio版本
```wiki
docker pull minio/minio:RELEASE.2023-03-24T21-41-23Z
```




## minio支持的事件
```java
        List<EventType> supportedEventList = Arrays.asList(
        OBJECT_CREATED_ANY,
        OBJECT_CREATED_PUT,
        OBJECT_CREATED_POST,
        OBJECT_CREATED_COPY,
        OBJECT_CREATED_COMPLETE_MULTIPART_UPLOAD,
        OBJECT_ACCESSED_GET,
        OBJECT_ACCESSED_HEAD,
        OBJECT_ACCESSED_ANY,
        OBJECT_REMOVED_ANY,
        OBJECT_REMOVED_DELETE,
        OBJECT_REMOVED_DELETED_MARKER_CREATED
        );


```


## 官方文档中的事件类型
```wiki
https://min.io/docs/minio/container/administration/monitoring/bucket-notifications.html

Supported S3 Event Types
MinIO bucket notifications are compatible with Amazon S3 Event Notifications. This section lists all supported events.

Object Events
MinIO supports triggering notifications on the following S3 object events:

s3:ObjectAccessed:Get
s3:ObjectAccessed:GetLegalHold
s3:ObjectAccessed:GetRetention
s3:ObjectAccessed:Head
s3:ObjectCreated:CompleteMultipartUpload
s3:ObjectCreated:Copy
s3:ObjectCreated:DeleteTagging
s3:ObjectCreated:Post
s3:ObjectCreated:Put
s3:ObjectCreated:PutLegalHold
s3:ObjectCreated:PutRetention
s3:ObjectCreated:PutTagging
s3:ObjectRemoved:Delete
s3:ObjectRemoved:DeleteMarkerCreated
Specify the wildcard * character to select all events related to a prefix:

s3:ObjectAccessed:*
Selects all s3:ObjectAccessed-prefixed events.

s3:ObjectCreated:*
Selects all s3:ObjectCreated-prefixed events.

s3:ObjectRemoved:*
Selects all s3:ObjectRemoved-prefixed events.

Replication Events
MinIO supports triggering notifications on the following S3 replication events:

s3:Replication:OperationCompletedReplication
s3:Replication:OperationFailedReplication
s3:Replication:OperationMissedThreshold
s3:Replication:OperationNotTracked
s3:Replication:OperationReplicatedAfterThreshold
Specify the wildcard * character to select all s3:Replication events:

s3:Replication:*
ILM Transition Events
MinIO supports triggering notifications on the following S3 ILM transition events:

s3:ObjectRestore:Post
s3:ObjectRestore:Completed
s3:ObjectTransition:Failed
s3:ObjectTransition:Complete
Specify the wildcard * character to select all events related to a prefix:

s3:ObjectTransition:*
Selects all s3:ObjectTransition-prefixed events.

s3:ObjectRestore:*
Selects all s3:ObjectRestore-prefixed events.

Global Events
MinIO supports triggering notifications on the following global events. You can only listen to these events through the ListenNotification <https://min.io/docs/minio/linux/developers/go/API.html#listennotification-context-context-context-prefix-suffix-string-events-string-chan-notification-info> API:

s3:BucketCreated
s3:BucketRemoved
```

## 项目介绍

### 事件监听器

```java
com.atom.minio.listener.MinioEventListener
```

### 事件处理器

```java
com.atom.minio.handler.EventHandler
```

