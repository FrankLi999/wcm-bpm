package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.dto.MetaData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type meta data refresh.
 */
@Slf4j
@RequiredArgsConstructor
public class MetaDataRefresh extends AbstractDataRefresh<MetaData> {

    private final List<MetaDataSubscriber> metaDataSubscribers;

    @Override
    protected JsonNode convert(final JsonNode data) {
        return data.get(ConfigGroupEnum.META_DATA.name());
    }

    @Override
    protected ConfigData<MetaData> fromJson(final JsonNode data) {
    	return JsonUtils.fromJson(data,new TypeReference<ConfigData<MetaData>>() { });
    }

    @Override
    protected boolean updateCacheIfNeed(final ConfigData<MetaData> result) {
        return updateCacheIfNeed(result, ConfigGroupEnum.META_DATA);
    }

    @Override
    public ConfigData<?> cacheConfigData() {
        return GROUP_CACHE.get(ConfigGroupEnum.META_DATA);
    }

    @Override
    protected void refresh(final List<MetaData> data) {
        if (ObjectUtils.isEmpty(data)) {
            log.info("clear all metaData cache");
            metaDataSubscribers.forEach(MetaDataSubscriber::refresh);
        } else {
            data.forEach(metaData -> metaDataSubscribers.forEach(subscriber -> subscriber.onSubscribe(metaData)));
        }
    }
}
