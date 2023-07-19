package com.atom.minio.enums;

import com.atom.minio.handler.*;
import io.minio.messages.EventType;

import java.util.Objects;

import static io.minio.messages.EventType.*;

/**
 * @author Atom
 */
public enum HandlerEnum {

    CREATE_OBJECT(new EventType[]{
            OBJECT_CREATED_ANY,
            OBJECT_CREATED_PUT,
            OBJECT_CREATED_POST,
            OBJECT_CREATED_COPY,
            OBJECT_CREATED_COMPLETE_MULTIPART_UPLOAD},
            CreateObjectEventHandler.class,
            "创建对象相关事件"),
    DELETE_OBJECT(new EventType[]{OBJECT_REMOVED_DELETE},
            DeleteObjectEventHandler.class,
            "删除对象相关事件"),

    DOWNLOAD_OBJECT(new EventType[]{OBJECT_ACCESSED_GET},
            DownloadObjectEventHandler.class,
            "下载对象相关事件"),

    VIEW_OBJECT(new EventType[]{OBJECT_ACCESSED_HEAD},
            ViewObjectEventHandler.class,
            "对象被查看相关事件");

    private EventType[] eventType;

    private Class<?> handlerClass;

    private String desc;


    HandlerEnum(EventType[] eventType, Class<?> handlerClass, String desc) {
        this.eventType = eventType;
        this.handlerClass = handlerClass;
        this.desc = desc;
    }

    public static EventHandler fromEventType(EventType eventType) throws InstantiationException, IllegalAccessException {
        for (HandlerEnum handlerEnum : HandlerEnum.values()) {
            for (EventType type : handlerEnum.getEventType()) {
                if (type == eventType) {
                    return (EventHandler) handlerEnum.getHandlerClass().newInstance();
                }
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + Objects.requireNonNull(eventType));
    }

    public EventType[] getEventType() {
        return eventType;
    }

    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    public String getDesc() {
        return desc;
    }
}
