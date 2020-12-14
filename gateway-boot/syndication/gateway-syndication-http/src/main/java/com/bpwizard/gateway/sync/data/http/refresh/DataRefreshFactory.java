package com.bpwizard.gateway.sync.data.http.refresh;

import java.util.EnumMap;
import java.util.List;

import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The type Data refresh factory.
 */
public final class DataRefreshFactory {

    private static final EnumMap<ConfigGroupEnum, DataRefresh> ENUM_MAP = new EnumMap<>(ConfigGroupEnum.class);

    /**
     * Instantiates a new Data refresh factory.
     *
     * @param pluginDataSubscriber the plugin data subscriber
     * @param metaDataSubscribers  the meta data subscribers
     * @param authDataSubscribers  the auth data subscribers
     */
    public DataRefreshFactory(final PluginDataSubscriber pluginDataSubscriber,
                              final List<MetaDataSubscriber> metaDataSubscribers,
                              final List<AuthDataSubscriber> authDataSubscribers) {
        ENUM_MAP.put(ConfigGroupEnum.PLUGIN, new PluginDataRefresh(pluginDataSubscriber));
        ENUM_MAP.put(ConfigGroupEnum.SELECTOR, new SelectorDataRefresh(pluginDataSubscriber));
        ENUM_MAP.put(ConfigGroupEnum.RULE, new RuleDataRefresh(pluginDataSubscriber));
        ENUM_MAP.put(ConfigGroupEnum.APP_AUTH, new AppAuthDataRefresh(authDataSubscribers));
        ENUM_MAP.put(ConfigGroupEnum.META_DATA, new MetaDataRefresh(metaDataSubscribers));
    }

    /**
     * Executor.
     *
     * @param data the data
     * @return the boolean
     */
    public boolean executor(final JsonNode data) {
        final boolean[] success = {false};
        ENUM_MAP.values().parallelStream().forEach(dataRefresh -> success[0] = dataRefresh.refresh(data));
        return success[0];
    }

    /**
     * Cache config data.
     *
     * @param group the group
     * @return the config data
     */
    public ConfigData<?> cacheConfigData(final ConfigGroupEnum group) {
        return ENUM_MAP.get(group).cacheConfigData();
    }
}
