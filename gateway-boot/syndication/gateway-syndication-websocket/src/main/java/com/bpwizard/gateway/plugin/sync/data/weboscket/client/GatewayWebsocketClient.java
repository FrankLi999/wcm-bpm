package com.bpwizard.gateway.plugin.sync.data.weboscket.client;

import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import com.bpwizard.gateway.common.dto.WebsocketData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.enums.DataEventTypeEnum;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.bpwizard.gateway.plugin.sync.data.weboscket.handler.WebsocketDataHandler;

/**
 * The type Gateway websocket client.
 */
@Slf4j
public final class GatewayWebsocketClient extends WebSocketClient {
    
    private volatile boolean alreadySync = Boolean.FALSE;
    
    private final WebsocketDataHandler websocketDataHandler;
    
    /**
     * Instantiates a new Geteway websocket client.
     *
     * @param serverUri             the server uri
     * @param pluginDataSubscriber the plugin data subscriber
     * @param metaDataSubscribers   the meta data subscribers
     * @param authDataSubscribers   the auth data subscribers
     */
    public GatewayWebsocketClient(final URI serverUri, final PluginDataSubscriber pluginDataSubscriber,
                               final List<MetaDataSubscriber> metaDataSubscribers, final List<AuthDataSubscriber> authDataSubscribers) {
        super(serverUri);
        this.websocketDataHandler = new WebsocketDataHandler(pluginDataSubscriber, metaDataSubscribers, authDataSubscribers);
    }
    
    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        if (!alreadySync) {
            send(DataEventTypeEnum.MYSELF.name());
            alreadySync = true;
        }
    }
    
    @Override
    public void onMessage(final String result) {
        handleResult(result);
    }
    
    @Override
    public void onClose(final int i, final String s, final boolean b) {
        this.close();
    }
    
    @Override
    public void onError(final Exception e) {
        this.close();
    }
    
    @SuppressWarnings("ALL")
    private void handleResult(final String result) {
        WebsocketData websocketData = JsonUtils.fromJson(result, WebsocketData.class);
        ConfigGroupEnum groupEnum = ConfigGroupEnum.acquireByName(websocketData.getGroupType());
        String eventType = websocketData.getEventType();
        String json = JsonUtils.toJson(websocketData.getData());
        websocketDataHandler.executor(groupEnum, json, eventType);
    }
}
