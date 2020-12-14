package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

/**
 * The interface Data handler.
 *
 */
public interface DataHandler {

    /**
     * Handle.
     *
     * @param json  the data for json
     * @param eventType the event type
     */
    void handle(String json, String eventType);
}
