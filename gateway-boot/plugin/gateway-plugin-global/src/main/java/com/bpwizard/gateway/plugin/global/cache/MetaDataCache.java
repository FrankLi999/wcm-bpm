package com.bpwizard.gateway.plugin.global.cache;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.bpwizard.gateway.common.dto.MetaData;
import com.bpwizard.gateway.common.utils.PathMatchUtils;

/**
 * The type Meta data cache.
 */
public final class MetaDataCache {
    
    private static final MetaDataCache INSTANCE = new MetaDataCache();
    
    /**
     * path -> MetaData.
     */
    private static final ConcurrentMap<String, MetaData> META_DATA_MAP = new ConcurrentHashMap<>();
    
    private MetaDataCache() {
    }
    
    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MetaDataCache getInstance() {
        return INSTANCE;
    }
    
    /**
     * Cache auth data.
     *
     * @param data the data
     */
    public void cache(final MetaData data) {
        META_DATA_MAP.put(data.getPath(), data);
    }
    
    /**
     * Remove auth data.
     *
     * @param data the data
     */
    public void remove(final MetaData data) {
        META_DATA_MAP.remove(data.getPath());
    }
    
    /**
     * Obtain auth data meta data.
     *
     * @param path the path
     * @return the meta data
     */
    public MetaData obtain(final String path) {
        MetaData metaData = META_DATA_MAP.get(path);
        if (Objects.isNull(metaData)) {
            String key = META_DATA_MAP.keySet().stream().filter(k -> PathMatchUtils.match(k, path)).findFirst().orElse("");
            return META_DATA_MAP.get(key);
        }
        return metaData;
    }
}
