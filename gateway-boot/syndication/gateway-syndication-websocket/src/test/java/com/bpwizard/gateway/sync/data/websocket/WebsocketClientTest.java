package com.bpwizard.gateway.sync.data.websocket;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.websocket;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

/**
 * The type Websocket client test.
 */
public final class WebsocketClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketClientTest.class);
    
    private static WebSocketClient client;
    
    private static Undertow server;
    
    @BeforeAll
    public static void init() {
        server = Undertow.builder()
                .addHttpListener(8888, "localhost")
                .setHandler(path()
                        .addPrefixPath("/websocket", websocket((exchange, channel) -> {
                            channel.getReceiveSetter().set(new AbstractReceiveListener() {

                                @Override
                                protected void onFullTextMessage(final WebSocketChannel channel, final BufferedTextMessage message) {
                                    WebSockets.sendText(message.getData(), channel, null);
                                }
                            });
                            channel.resumeReceives();
                        })))
                .build();
        server.start();
    }
    
    @AfterAll
    public static void after() {
        server.stop();
    }
    
    @AfterEach
    public void destroy() {
        client.close();
    }
    
    @Test
    public void send() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        client = new WebSocketClient(new URI("ws://localhost:8888/websocket")) {
            @Override
            public void onOpen(final ServerHandshake serverHandshake) {
                LOGGER.info("Open connection");
            }

            @Override
            public void onMessage(final String s) {
                latch.countDown();
            }

            @Override
            public void onClose(final int i, final String s, final boolean b) {
            }

            @Override
            public void onError(final Exception e) {
                LOGGER.error("", e);
            }
        };
        client.connect();
        while (!client.getReadyState().equals(ReadyState.OPEN)) {
            LOGGER.debug("connecting...");
        }
        client.send("xiaoyu");
        latch.await(3, TimeUnit.SECONDS);
    }
}
