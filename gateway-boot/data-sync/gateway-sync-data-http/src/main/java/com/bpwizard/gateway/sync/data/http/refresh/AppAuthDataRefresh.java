package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.bpwizard.gateway.common.dto.AppAuthData;
import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type app auth data refresh.
 */
@Slf4j
@RequiredArgsConstructor
public class AppAuthDataRefresh extends AbstractDataRefresh<AppAuthData> {

    private final List<AuthDataSubscriber> authDataSubscribers;

    @Override
    protected JsonNode convert(final JsonNode data) {
        return data.get(ConfigGroupEnum.APP_AUTH.name());
    }

    @Override
    protected ConfigData<AppAuthData> fromJson(final JsonNode data) {
        return JsonUtils.fromJson(data,new TypeReference<ConfigData<AppAuthData>>() { });
    }

    @Override
    protected boolean updateCacheIfNeed(final ConfigData<AppAuthData> result) {
        return updateCacheIfNeed(result, ConfigGroupEnum.APP_AUTH);
    }

    @Override
    public ConfigData<?> cacheConfigData() {
        return GROUP_CACHE.get(ConfigGroupEnum.APP_AUTH);
    }

    @Override
    protected void refresh(final List<AppAuthData> data) {
        if (ObjectUtils.isEmpty(data)) {
            log.info("clear all appAuth data cache");
            authDataSubscribers.forEach(AuthDataSubscriber::refresh);
        } else {
            data.forEach(authData -> authDataSubscribers.forEach(subscriber -> subscriber.onSubscribe(authData)));
        }
    }
}
