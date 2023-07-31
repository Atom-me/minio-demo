## 安装mc

```shell
#macos
brew install minio/stable/mc

#配置远程minio服务器连接信息
mc alias set test_minio http://1.1.1.1:9000 admin 12345678
#查看 test_minio对应的远端minio服务器信息
mc admin info test_minio

```

## 在minio client的可执行目录下执行如下命令，查看是否已经存在Kafka通知配置

> notify_kafka enable=off topic= brokers= sasl_username= sasl_password= sasl_mechanism=plain client_tls_cert= client_tls_key= tls_client_auth=0 sasl=off tls=off tls_skip_verify=off queue_limit=0 queue_dir= version=
>
> 说明没配置过

```shell
mc admin config get test_minio/ notify_kafka

notify_kafka enable=off topic= brokers= sasl_username= sasl_password= sasl_mechanism=plain client_tls_cert= client_tls_key= tls_client_auth=0 sasl=off tls=off tls_skip_verify=off queue_limit=0 queue_dir= version=
➜  ~

```

## 添加Kafka通知配置

```shell
mc admin config set test_minio/ notify_kafka:test_kafka \
   brokers="1.1.1.1:9092" \
   topic="test_minio_topic" \
   comment="atom test config"
```

## 通过mc重启minio服务器

```shell
mc admin service restart test_minio/
```

> 重启之后，minio服务器会打印日志：
>
> SQS ARNs: arn:minio:sqs::test_kafka:kafka

```shell
#启动一个consumer消费消息
bash-5.1# /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 -from-beginning --topic test_minio_topic

```

## kafka topic 

> kafka topic会自动创建，无需人工创建。

## 测试

### 创建bucket

```shell
mc mb test_minio/my-bucket
```

### 添加事件

#### 查看对应ARN信息

> 给bucket添加事件时，ARN 是 必填参数，
>
> 查看ARN信息：
>
> mc admin info --json test_minio
>
> 1. This returns the ARN to use for notifications, such as `arn:minio:sqs::primary:kafka`
>
> ```shell
> 
> ➜  ~ mc admin info --json test_minio
> {
>     "status": "success",
>     "info": {
>         "mode": "online",
>         "sqsARN": [
>             "arn:minio:sqs::test_kafka:kafka"
>         ],
>         ......
>         ......
> ➜  ~
> ```

#### 给对应bucket添加事件，必须指定ARN参数

```shell
mc event add --event "put,get,delete" test_minio/my-bucket arn:minio:sqs::test_kafka:kafka

mc event ls test_minio/my-bucket arn:minio:sqs::test_kafka:kafka
mc event ls test_minio/my-bucket

```

### 上传文件

```shell
echo aaaaa > ~/temp/a.txt
mc cp ~/temp/a.txt test_minio/my-bucket

```

## 查看topic

> 自动创建了topic： test_minio_topic

```shell
bash-5.1# /opt/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
__consumer_offsets
test_minio_topic
```

## 查看事件消息

> 直接开启一个消费者，消费topic就行
>
>  /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 -from-beginning --topic test_minio_topic

```shell
bash-5.1# /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 -from-beginning --topic test_minio_topic
{"EventName":"s3:ObjectCreated:Put","Key":"my-bucket/a.txt","Records":[{"eventVersion":"2.0","eventSource":"minio:s3","awsRegion":"","eventTime":"2023-07-31T07:44:17.447Z","eventName":"s3:ObjectCreated:Put","userIdentity":{"principalId":"admin"},"requestParameters":{"principalId":"admin","region":"","sourceIPAddress":"112.96.227.159"},"responseElements":{"x-amz-id-2":"dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8","x-amz-request-id":"1776E40DA6025FF7","x-minio-deployment-id":"2dca52d0-8db2-49b3-ba26-d5e75a686388","x-minio-origin-endpoint":"http://172.17.0.2:9000"},"s3":{"s3SchemaVersion":"1.0","configurationId":"Config","bucket":{"name":"my-bucket","ownerIdentity":{"principalId":"admin"},"arn":"arn:aws:s3:::my-bucket"},"object":{"key":"a.txt","size":6,"eTag":"4c850c5b3b2756e67a91bad8e046ddac","contentType":"text/plain","userMetadata":{"content-type":"text/plain"},"sequencer":"1776E40DA91D3682"}},"source":{"host":"112.96.227.159","port":"","userAgent":"MinIO (darwin; amd64) minio-go/v7.0.57 mc/RELEASE.2023-07-21T20-44-27Z"}}]}
{"EventName":"s3:ObjectCreated:Put","Key":"my-bucket/需求.md","Records":[{"eventVersion":"2.0","eventSource":"minio:s3","awsRegion":"","eventTime":"2023-07-31T07:46:52.576Z","eventName":"s3:ObjectCreated:Put","userIdentity":{"principalId":"admin"},"requestParameters":{"principalId":"admin","region":"","sourceIPAddress":"112.96.227.159"},"responseElements":{"x-amz-id-2":"dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8","x-amz-request-id":"1776E431C7209599","x-minio-deployment-id":"2dca52d0-8db2-49b3-ba26-d5e75a686388","x-minio-origin-endpoint":"http://172.17.0.2:9000"},"s3":{"s3SchemaVersion":"1.0","configurationId":"Config","bucket":{"name":"my-bucket","ownerIdentity":{"principalId":"admin"},"arn":"arn:aws:s3:::my-bucket"},"object":{"key":"%E9%9C%80%E6%B1%82.md","size":621,"eTag":"dff2b1c865a263c0b04becac72f698f9","contentType":"text/markdown","userMetadata":{"content-type":"text/markdown"},"sequencer":"1776E431C78B16E1"}},"source":{"host":"112.96.227.159","port":"","userAgent":"MinIO (linux; amd64) minio-go/v7.0.61 MinIO Console/(dev)"}}]}
{"EventName":"s3:ObjectAccessed:Head","Key":"my-bucket/b.txt","Records":[{"eventVersion":"2.0","eventSource":"minio:s3","awsRegion":"","eventTime":"2023-07-31T07:53:34.428Z","eventName":"s3:ObjectAccessed:Head","userIdentity":{"principalId":"admin"},"requestParameters":{"principalId":"admin","region":"","sourceIPAddress":"112.96.227.159"},"responseElements":{"content-length":"6","x-amz-id-2":"dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8","x-amz-request-id":"1776E48F5767F9A0","x-minio-deployment-id":"2dca52d0-8db2-49b3-ba26-d5e75a686388","x-minio-origin-endpoint":"http://172.17.0.2:9000"},"s3":{"s3SchemaVersion":"1.0","configurationId":"Config","bucket":{"name":"my-bucket","ownerIdentity":{"principalId":"admin"},"arn":"arn:aws:s3:::my-bucket"},"object":{"key":"b.txt","size":6,"eTag":"d8c36aa3fc98f2e3a3a0491699d2b022","contentType":"text/plain","userMetadata":{"content-type":"text/plain"},"sequencer":"1776E48F57CD0733"}},"source":{"host":"112.96.227.159","port":"","userAgent":"MinIO (linux; amd64) minio-go/v7.0.61 MinIO Console/(dev)"}}]}
{"EventName":"s3:ObjectRemoved:Delete","Key":"my-bucket/b.txt","Records":[{"eventVersion":"2.0","eventSource":"minio:s3","awsRegion":"","eventTime":"2023-07-31T07:53:39.740Z","eventName":"s3:ObjectRemoved:Delete","userIdentity":{"principalId":"admin"},"requestParameters":{"principalId":"admin","region":"","sourceIPAddress":"127.0.0.1"},"responseElements":{"content-length":"151","x-amz-id-2":"dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8","x-amz-request-id":"1776E490645DAD93","x-minio-deployment-id":"2dca52d0-8db2-49b3-ba26-d5e75a686388","x-minio-origin-endpoint":"http://172.17.0.2:9000"},"s3":{"s3SchemaVersion":"1.0","configurationId":"Config","bucket":{"name":"my-bucket","ownerIdentity":{"principalId":"admin"},"arn":"arn:aws:s3:::my-bucket"},"object":{"key":"b.txt","sequencer":"1776E4909466A32B"}},"source":{"host":"127.0.0.1","port":"","userAgent":"MinIO (linux; amd64) minio-go/v7.0.61 MinIO Console/(dev)"}}]}

```

