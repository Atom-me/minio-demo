package com.atom.minio.handler;

import io.minio.messages.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 对象被访问的时候会触发s3:ObjectAccessed:Head事件
 *
 * @author Atom
 */
@Component
public class ViewObjectEventHandler implements EventHandler<Event> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewObjectEventHandler.class);

    @Override
    public void handle(Event event) {
        LOGGER.info("........................start handle view object event........................");
        LOGGER.info("event type :[{}]", event.eventType());
        LOGGER.info("event time :[{}]", event.eventTime());
        LOGGER.info("event bucketName :[{}]", event.bucketName());
        LOGGER.info("event bucketOwner :[{}]", event.bucketOwner());
        LOGGER.info("event objectName :[{}]", event.objectName());
        LOGGER.info("event ObjectSize :[{}]", event.objectSize());
        LOGGER.info("event host :[{}]", event.host());
        LOGGER.info("event port :[{}]", event.port());
        LOGGER.info("event objectVersionId :[{}]", event.objectVersionId());
        LOGGER.info("event userAgent :[{}]", event.userAgent());
        LOGGER.info("event bucketArn :[{}]", event.bucketArn());
        LOGGER.info("event etag :[{}]", event.etag());
        LOGGER.info("event region :[{}]", event.region());
        LOGGER.info("event userMetadata :[{}]", event.userMetadata());
        LOGGER.info("event userId :[{}]", event.userId());
    }
}
