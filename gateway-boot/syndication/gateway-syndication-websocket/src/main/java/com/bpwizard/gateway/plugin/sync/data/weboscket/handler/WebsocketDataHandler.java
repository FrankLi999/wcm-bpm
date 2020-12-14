package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import java.util.EnumMap;
import java.util.List;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;

/**
 * The type Websocket cache handler.
 */
public class WebsocketDataHandler {

    private static final EnumMap<ConfigGroupEnum, DataHandler> ENUM_MAP = new EnumMap<>(ConfigGroupEnum.class);

    /**
     * Instantiates a new Websocket data handler.
     *
     * @param pluginDataSubscriber the plugin data subscriber
     * @param metaDataSubscribers  the meta data subscribers
     * @param authDataSubscribers  the auth data subscribers
     */
    public WebsocketDataHandler(final PluginDataSubscriber pluginDataSubscriber,
                                final List<MetaDataSubscriber> metaDataSubscribers,
                                final List<AuthDataSubscriber> authDataSubscribers) {
        ENUM_MAP.put(ConfigGroupEnum.PLUGIN, new PluginDataHandler(pluginDataSubscriber));
        ENUM_MAP.put(ConfigGroupEnum.SELECTOR, new SelectorDataHandler(pluginDataSubscriber));
        ENUM_MAP.put(ConfigGroupEnum.RULE, new RuleDataHandler(pluginDataSubscriber));
        ENUM_MAP.put(ConfigGroupEnum.APP_AUTH, new AuthDataHandler(authDataSubscribers));
        ENUM_MAP.put(ConfigGroupEnum.META_DATA, new MetaDataHandler(metaDataSubscribers));
    }

    /**
     * Executor.
     *
     * @param type      the type
     * @param json      the json
     * @param eventType the event type
     */
    public void executor(final ConfigGroupEnum type, final String json, final String eventType) {
        ENUM_MAP.get(type).handle(json, eventType);
    }
}
