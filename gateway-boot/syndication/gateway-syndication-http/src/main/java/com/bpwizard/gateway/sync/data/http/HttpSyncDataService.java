package com.bpwizard.gateway.sync.data.http;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.gateway.common.concurrent.GatewayThreadFactory;
import com.bpwizard.gateway.common.constant.HttpConstants;
import com.bpwizard.gateway.common.dto.ConfigData;
import com.bpwizard.gateway.common.enums.ConfigGroupEnum;
import com.bpwizard.gateway.common.exception.GatewayException;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.common.utils.ThreadUtils;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.bpwizard.gateway.sync.data.api.SyncDataService;
import com.bpwizard.gateway.sync.data.http.config.HttpConfig;
import com.bpwizard.gateway.sync.data.http.refresh.DataRefreshFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP long polling implementation.
 */
@SuppressWarnings("all")
@Slf4j
public class HttpSyncDataService implements SyncDataService, AutoCloseable {

    private static final AtomicBoolean RUNNING = new AtomicBoolean(false);

    /**
     * default: 10s.
     */
    private Duration connectionTimeout = Duration.ofSeconds(10);

    /**
     * only use for http long polling.
     */
    private RestTemplate httpClient;

    private ExecutorService executor;

    private HttpConfig httpConfig;

    private List<String> serverList;

    private DataRefreshFactory factory;

    public HttpSyncDataService(final HttpConfig httpConfig, final PluginDataSubscriber pluginDataSubscriber,
                               final List<MetaDataSubscriber> metaDataSubscribers, final List<AuthDataSubscriber> authDataSubscribers) {
        this.factory = new DataRefreshFactory(pluginDataSubscriber, metaDataSubscribers, authDataSubscribers);
        this.httpConfig = httpConfig;
        this.serverList = Arrays.asList(httpConfig.getUrl().split(","));
        this.httpClient = createRestTemplate();
        this.start();
    }
    
    private RestTemplate createRestTemplate() {
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
        factory.setConnectTimeout((int) this.connectionTimeout.toMillis());
        factory.setReadTimeout((int) HttpConstants.CLIENT_POLLING_READ_TIMEOUT);
        return new RestTemplate(factory);
    }

    private void start() {
        // It could be initialized multiple times, so you need to control that.
        if (RUNNING.compareAndSet(false, true)) {
            // fetch all group configs.
            this.fetchGroupConfig(ConfigGroupEnum.values());
            int threadSize = serverList.size();
            this.executor = new ThreadPoolExecutor(threadSize, threadSize, 60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    GatewayThreadFactory.create("http-long-polling", true));
            // start long polling, each server creates a thread to listen for changes.
            this.serverList.forEach(server -> this.executor.execute(new HttpLongPollingTask(server)));
        } else {
            log.info("gateway http long polling was started, executor=[{}]", executor);
        }
    }

    private void fetchGroupConfig(final ConfigGroupEnum... groups) throws GatewayException {
        for (int index = 0; index < this.serverList.size(); index++) {
            String server = serverList.get(index);
            try {
                this.doFetchGroupConfig(server, groups);
                break;
            } catch (GatewayException e) {
                // no available server, throw exception.
                if (index >= serverList.size() - 1) {
                    throw e;
                }
                log.warn("fetch config fail, try another one: {}", serverList.get(index + 1));
            }
        }
    }

    private void doFetchGroupConfig(final String server, final ConfigGroupEnum... groups) {
        StringBuilder params = new StringBuilder();
        for (ConfigGroupEnum groupKey : groups) {
            params.append("groupKeys").append("=").append(groupKey.name()).append("&");
        }
        String url = server + "/configs/fetch?" + StringUtils.removeEnd(params.toString(), "&");
        log.info("request configs: [{}]", url);
        String json = null;
        try {
            json = this.httpClient.getForObject(url, String.class);
        } catch (RestClientException e) {
            String message = String.format("fetch config fail from server[%s], %s", url, e.getMessage());
            log.warn(message);
            throw new GatewayException(message, e);
        }
        // update local cache
        boolean updated = this.updateCacheWithJson(json);
        if (updated) {
            log.info("get latest configs: [{}]", json);
            return;
        }
        // not updated. it is likely that the current config server has not been updated yet. wait a moment.
        log.info("The config of the server[{}] has not been updated or is out of date. Wait for 30s to listen for changes again.", server);
        ThreadUtils.sleep(TimeUnit.SECONDS, 30);
    }

    /**
     * update local cache.
     * @param json the response from config server.
     * @return true: the local cache was updated. false: not updated.
     */
    private boolean updateCacheWithJson(final String json) {
        JsonNode jsonObject = JsonUtils.readTree(json);
        JsonNode data = jsonObject.get("data");
        // if the config cache will be updated?
        return factory.executor(data);
    }

    @SuppressWarnings("unchecked")
    private void doLongPolling(final String server) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(8);
        for (ConfigGroupEnum group : ConfigGroupEnum.values()) {
            ConfigData<?> cacheConfig = factory.cacheConfigData(group);
            String value = String.join(",", cacheConfig.getMd5(), String.valueOf(cacheConfig.getLastModifyTime()));
            params.put(group.name(), Arrays.asList(value));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(params, headers);
        String listenerUrl = server + "/configs/listener";
        log.debug("request listener configs: [{}]", listenerUrl);
        ArrayNode groupJson = null;
        try {
            String json = this.httpClient.postForEntity(listenerUrl, httpEntity, String.class).getBody();
            log.debug("listener result: [{}]", json);
            groupJson = (ArrayNode)JsonUtils.readTree(json).get("data");
        } catch (RestClientException e) {
            String message = String.format("listener configs fail, server:[%s], %s", server, e.getMessage());
            throw new GatewayException(message, e);
        }
        if (groupJson != null) {
            // fetch group configuration async.
            ConfigGroupEnum[] changedGroups = JsonUtils.fromJson(groupJson, ConfigGroupEnum[].class);
            if (ArrayUtils.isNotEmpty(changedGroups)) {
                log.info("Group config changed: {}", Arrays.toString(changedGroups));
                this.doFetchGroupConfig(server, changedGroups);
            }
        }
    }

    @Override
    public void close() throws Exception {
        RUNNING.set(false);
        if (executor != null) {
            executor.shutdownNow();
            // help gc
            executor = null;
        }
    }

    class HttpLongPollingTask implements Runnable {

        private String server;

        private final int retryTimes = 3;

        HttpLongPollingTask(final String server) {
            this.server = server;
        }

        @Override
        public void run() {
            while (RUNNING.get()) {
                for (int time = 1; time <= retryTimes; time++) {
                    try {
                        doLongPolling(server);
                    } catch (Exception e) {
                        // print warnning log.
                        if (time < retryTimes) {
                            log.warn("Long polling failed, tried {} times, {} times left, will be suspended for a while! {}",
                                    time, retryTimes - time, e.getMessage());
                            ThreadUtils.sleep(TimeUnit.SECONDS, 5);
                            continue;
                        }
                        // print error, then suspended for a while.
                        log.error("Long polling failed, try again after 5 minutes!", e);
                        ThreadUtils.sleep(TimeUnit.MINUTES, 5);
                    }
                }
            }
            log.warn("Stop http long polling.");
        }
    }
}
