package com.atom.minio.handler;

import com.atom.minio.enums.HandlerEnum;
import io.minio.messages.EventType;
import org.springframework.stereotype.Component;

/**
 * @author Atom
 */
@Component
public class EventHandlerFactory {
    public EventHandler getHandler(EventType eventType) throws InstantiationException, IllegalAccessException {
        return HandlerEnum.fromEventType(eventType);
    }
}
