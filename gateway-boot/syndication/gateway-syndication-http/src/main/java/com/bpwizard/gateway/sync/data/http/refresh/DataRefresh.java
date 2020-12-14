package com.bpwizard.gateway.sync.data.http.refresh;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The interface Data refresh.
 */
public interface DataRefresh {

    /**
     * Refresh boolean.
     *
     * @param data the data
     * @return the boolean
     */
    Boolean refresh(JsonNode data);

    /**
     * Cache config data config data.
     *
     * @return the config data
     */
    ConfigData<?> cacheConfigData();
}
