package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.SelectorData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Selector data refresh.
 */
@Slf4j
@RequiredArgsConstructor
public class SelectorDataRefresh extends AbstractDataRefresh<SelectorData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    protected JsonNode convert(final JsonNode data) {
        return data.get(ConfigGroupEnum.SELECTOR.name());
    }

    @Override
    protected ConfigData<SelectorData> fromJson(final JsonNode data) {
    	return JsonUtils.fromJson(data,new TypeReference<ConfigData<SelectorData>>() { });
    }

    @Override
    protected boolean updateCacheIfNeed(final ConfigData<SelectorData> result) {
        return updateCacheIfNeed(result, ConfigGroupEnum.SELECTOR);
    }

    @Override
    public ConfigData<?> cacheConfigData() {
        return GROUP_CACHE.get(ConfigGroupEnum.SELECTOR);
    }

    @Override
    protected void refresh(final List<SelectorData> data) {
        if (ObjectUtils.isEmpty(data)) {
            log.info("clear all selector cache, old cache");
            data.forEach(pluginDataSubscriber::unSelectorSubscribe);
            pluginDataSubscriber.refreshSelectorDataAll();
        } else {
            // update cache for UpstreamCacheManager
            pluginDataSubscriber.refreshSelectorDataAll();
            data.forEach(pluginDataSubscriber::onSelectorSubscribe);
        }
    }
}
