package com.bpwizard.gateway.plugin.global.subsciber;

import com.bpwizard.gateway.common.dto.MetaData;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;

import com.bpwizard.gateway.plugin.global.cache.MetaDataCache;

/**
 * The type Meta data all subscriber.
 */
public class MetaDataAllSubscriber implements MetaDataSubscriber {
    
    @Override
    public void onSubscribe(final MetaData metaData) {
        MetaDataCache.getInstance().cache(metaData);
    }
    
    @Override
    public void unSubscribe(final MetaData metaData) {
        MetaDataCache.getInstance().remove(metaData);
    }
}
