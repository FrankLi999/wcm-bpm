package com.bpwizard.spring.boot.jgroup;

import io.undertow.Undertow;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatch;
import org.jgroups.JChannel;
import org.jgroups.raft.RaftHandle;
import org.jgroups.raft.blocks.ReplicatedStateMachine;

public class Webserver {

    public static class RaftNode {

        ReplicatedStateMachine<String, String> rsm;

        public RaftNode(String id) throws Exception {
            JChannel ch = new JChannel(Thread.currentThread().getContextClassLoader().getResourceAsStream("raft.xml"));

            rsm = new ReplicatedStateMachine<>(ch);
            RaftHandle handle = new RaftHandle(ch, rsm);
            handle.raftId(id);

            ch.connect("raft-cluster");
        }

        public String get(String key) {
            return rsm.get(key);
        }

        public void put(String key, String value) throws Exception {
            rsm.put(key, value);
        }

        public void remove(String key) throws Exception {
            rsm.remove(key);
        }

        public boolean exists(String key) {
            return get(key) != null;
        }
    }

    public static class UndertowServer {

        private final Undertow server;

        public UndertowServer(int port, final RaftNode raftNode) {
            PathTemplateHandler handler = new PathTemplateHandler();

            handler.add("/cluster/{key}/{value}", exchange -> {
                PathTemplateMatch attachment = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
                String key = attachment.getParameters().get("key");
                String value = attachment.getParameters().get("value");

                if (exchange.getRequestMethod().equals(HttpString.tryFromString("PUT"))) {
                    raftNode.put(key, value);
                    exchange.setResponseCode(200);
                }
            });

            handler.add("/cluster/{key}", exchange -> {
                PathTemplateMatch attachment = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
                String key = attachment.getParameters().get("key");

                if (!raftNode.exists(key)) {
                    exchange.setResponseCode(404);
                } else {
                    exchange.setResponseCode(200);
                    if (exchange.getRequestMethod().equals(HttpString.tryFromString("GET"))) {
                        exchange.getResponseSender().send(raftNode.get(key));
                    } else if (exchange.getRequestMethod().equals(HttpString.tryFromString("DELETE"))) {
                        raftNode.remove(key);
                    }
                }
            });

            server = Undertow.builder()
                    .addHttpListener(port, "localhost")
                    .setHandler(handler)
                    .build();
        }

        public UndertowServer start() {
            server.start();
            return this;
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true"); // osx prefers ipv6

        try {
            new UndertowServer(8080, new RaftNode("A")).start();
            new UndertowServer(8081, new RaftNode("B")).start();
            new UndertowServer(8082, new RaftNode("C")).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}