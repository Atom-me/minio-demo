package com.atom.minio.handler;


/**
 * @author Atom
 */
public interface EventHandler<Event> {

    /**
     * handle event
     *
     * @param event
     */
    void handle(Event event);

}
