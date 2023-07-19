package com.atom.minio.listener;


import com.atom.minio.handler.EventHandler;
import com.atom.minio.handler.EventHandlerFactory;
import io.minio.CloseableIterator;
import io.minio.ListenBucketNotificationArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Event;
import io.minio.messages.EventType;
import io.minio.messages.NotificationRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static io.minio.messages.EventType.*;

@Component
public class MinioEventListener implements CommandLineRunner {


    private static final Logger LOGGER = LoggerFactory.getLogger(MinioEventListener.class);

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private EventHandlerFactory eventHandlerFactory;

    @Override
    public void run(String... args) {
        // Exception: A specified event is not supported for notifications.
        // List<EventType> events = Arrays.asList(values());

        List<EventType> events = Arrays.asList(
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
                // Exception: A specified event is not supported for notifications.
//                REDUCED_REDUNDANCY_LOST_OBJECT
//                BUCKET_CREATED
//                BUCKET_REMOVED
                );
        String[] eventsStrArr = new String[events.size()];

        for (int i = 0; i < events.size(); i++) {
            eventsStrArr[i] = events.get(i).toString();
        }

        ListenBucketNotificationArgs listenBucketNotificationArgs =
                ListenBucketNotificationArgs.builder()
                        .bucket("atom-test")
                        .prefix("")
                        .suffix("")
                        .events(eventsStrArr)
                        .build();

        try (CloseableIterator<Result<NotificationRecords>> ci = minioClient.listenBucketNotification(listenBucketNotificationArgs)) {
            while (ci.hasNext()) {
                NotificationRecords records = ci.next().get();
                for (Event event : records.events()) {
                    LOGGER.info("start process event:::[{}]", event.eventType());
                    EventHandler handler = eventHandlerFactory.getHandler(event.eventType());
                    handler.handle(event);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred: ", e);
        }
    }
}
