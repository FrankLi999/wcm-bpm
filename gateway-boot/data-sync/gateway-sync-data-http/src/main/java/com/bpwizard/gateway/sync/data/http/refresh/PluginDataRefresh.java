package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Plugin data refresh.
 */
@Slf4j
@RequiredArgsConstructor
public class PluginDataRefresh extends AbstractDataRefresh<PluginData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    protected JsonNode convert(final JsonNode data) {
        return data.get(ConfigGroupEnum.PLUGIN.name());
    }

    @Override
    protected ConfigData<PluginData> fromJson(final JsonNode data) {
    	return JsonUtils.fromJson(data,new TypeReference<ConfigData<PluginData>>() { });
    }

    @Override
    protected boolean updateCacheIfNeed(final ConfigData<PluginData> result) {
        return updateCacheIfNeed(result, ConfigGroupEnum.PLUGIN);
    }

    @Override
    public ConfigData<?> cacheConfigData() {
        return GROUP_CACHE.get(ConfigGroupEnum.PLUGIN);
    }

    @Override
    protected void refresh(final List<PluginData> data) {
        if (ObjectUtils.isEmpty(data)) {
            log.info("clear all plugin data cache");
            pluginDataSubscriber.refreshPluginDataAll();
        } else {
            pluginDataSubscriber.refreshPluginDataAll();
            data.forEach(pluginDataSubscriber::onSubscribe);
        }
    }
}
