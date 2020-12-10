package com.bpwizard.gateway.sync.data.api;

import com.bpwizard.gateway.common.dto.MetaData;

/**
 * The interface Meta data subscriber.
 */
public interface MetaDataSubscriber {
    
    /**
     * On subscribe.
     *
     * @param metaData the meta data
     */
    void onSubscribe(MetaData metaData);
    
    /**
     * Un subscribe.
     *
     * @param metaData the meta data
     */
    void unSubscribe(MetaData metaData);
    
    /**
     * Refresh.
     */
    default void refresh() {
    }
}
