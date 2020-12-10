package com.bpwizard.gateway.sync.data.api;

import com.bpwizard.gateway.common.dto.AppAuthData;

/**
 * The interface Auth data subscriber.
 */
public interface AuthDataSubscriber {
    
    /**
     * On subscribe.
     *
     * @param appAuthData the app auth data
     */
    void onSubscribe(AppAuthData appAuthData);
    
    /**
     * Un subscribe.
     *
     * @param appAuthData the app auth data
     */
    void unSubscribe(AppAuthData appAuthData);
    
    /**
     * Refresh.
     */
    default void refresh() {
    }
}
