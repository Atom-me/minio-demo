package com.atom.minio.handler;


/**
 * @author Atom
 */
public interface EventHandler<Event> {

    void handle(Event event);

}
