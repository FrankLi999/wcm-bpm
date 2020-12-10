package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Abstract data refresh.
 *
 * @param <T> the type parameter
 */
@Slf4j
public abstract class AbstractDataRefresh<T> implements DataRefresh {

    /**
     * The Group cache.
     */
    protected static final ConcurrentMap<ConfigGroupEnum, ConfigData<?>> GROUP_CACHE = new ConcurrentHashMap<>();

    /**
     * Convert json object.
     *
     * @param data the data
     * @return the json object
     */
    protected abstract JsonNode convert(JsonNode data);

    /**
     * From json config data.
     *
     * @param data the data
     * @return the config data
     */
    protected abstract ConfigData<T> fromJson(JsonNode data);

    /**
     * Refresh.
     *
     * @param data the data
     */
    protected abstract void refresh(List<T> data);

    @Override
    public Boolean refresh(final JsonNode data) {
        boolean updated = false;
        JsonNode jsonObject = convert(data);
        if (null != jsonObject) {
            ConfigData<T> result = fromJson(jsonObject);
            if (this.updateCacheIfNeed(result)) {
                updated = true;
                refresh(result.getData());
            }
        }
        return updated;
    }

    /**
     * Update cache if need boolean.
     *
     * @param result the result
     * @return the boolean
     */
    protected abstract boolean updateCacheIfNeed(ConfigData<T> result);

    /**
     * If the MD5 values are different and the last update time of the old data is less than
     * the last update time of the new data, the configuration cache is considered to have been changed.
     *
     * @param newVal    the lasted config
     * @param groupEnum the group enum
     * @return true : if need update
     */
    protected boolean updateCacheIfNeed(final ConfigData<T> newVal, final ConfigGroupEnum groupEnum) {
        // first init cache
        if (GROUP_CACHE.putIfAbsent(groupEnum, newVal) == null) {
            return true;
        }
        ResultHolder holder = new ResultHolder(false);
        GROUP_CACHE.merge(groupEnum, newVal, (oldVal, value) -> {
            // must compare the last update time
            if (!oldVal.getMd5().equals(newVal.getMd5()) && oldVal.getLastModifyTime() < newVal.getLastModifyTime()) {
                log.info("update {} config: {}", groupEnum, newVal);
                holder.result = true;
                return newVal;
            }
            log.info("Get the same config, the [{}] config cache will not be updated, md5:{}", groupEnum, oldVal.getMd5());
            return oldVal;
        });
        return holder.result;
    }

    private static final class ResultHolder {

        private boolean result;

        /**
         * Instantiates a new Result holder.
         *
         * @param result the result
         */
        ResultHolder(final boolean result) {
            this.result = result;
        }
    }
}
