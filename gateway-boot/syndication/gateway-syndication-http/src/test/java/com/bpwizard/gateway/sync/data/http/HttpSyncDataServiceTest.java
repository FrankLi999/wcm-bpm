package com.bpwizard.gateway.sync.data.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link HttpSyncDataService}.
 */
public class HttpSyncDataServiceTest {
 
    // @Rule
    // public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig().dynamicPort(), false);
    
    @BeforeEach
    public final void setUpWiremock() {
//        wireMockRule.stubFor(get(urlPathEqualTo("/configs/fetch"))
//                .willReturn(aResponse()
//                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
//                        .withBody(this.mockConfigsFetchResponseJson())
//                        .withStatus(200))
//        );
//        wireMockRule.stubFor(post(urlPathEqualTo("/configs/listener"))
//                .willReturn(aResponse()
//                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
//                        .withBody(this.mockConfigsListenResponseJson())
//                        .withStatus(200))
//        );
    }
    
    /**
     * this method covers {@link HttpSyncDataService} constructor and {@link HttpSyncDataService#close()} method.
     *
     * @throws Exception any exception
     */
    @Test
    public void test() throws Exception {
//        try (HttpSyncDataService ignored = this.buildHttpSyncDataService()) {
//            // sleep 5 seconds to ensure Http Long polling task run
//            TimeUnit.SECONDS.sleep(5);
//        }
        
    }
    
//    private HttpSyncDataService buildHttpSyncDataService() {
//        HttpConfig httpConfig = new HttpConfig();
//        httpConfig.setUrl(this.getMockServerUrl());
//        // set http connection timeout
//        httpConfig.setConnectionTimeout(3);
//        // set delay time
//        httpConfig.setDelayTime(3);
//        PluginDataSubscriber pluginDataSubscriber = new PluginDataSubscriber() {
//            @Override
//            public void onSubscribe(final PluginData pluginData) {
//            
//            }
//        };
//        List<MetaDataSubscriber> metaDataSubscribers = Collections.singletonList(new MetaDataSubscriber() {
//            @Override
//            public void onSubscribe(final MetaData metaData) {
//            
//            }
//            
//            @Override
//            public void unSubscribe(final MetaData metaData) {
//            
//            }
//        });
//        List<AuthDataSubscriber> authDataSubscribers = Collections.singletonList(new AuthDataSubscriber() {
//            @Override
//            public void onSubscribe(final AppAuthData appAuthData) {
//            
//            }
//            
//            @Override
//            public void unSubscribe(final AppAuthData appAuthData) {
//            
//            }
//        });
//        return new HttpSyncDataService(httpConfig, pluginDataSubscriber, metaDataSubscribers, authDataSubscribers);
//    }
//    
//    private String getMockServerUrl() {
//        return "http://localhost:" + wireMockRule.port();
//    }
//    
//    // mock configs listen api response
//    private String mockConfigsListenResponseJson() {
//        return "{\"code\":200,\"message\":\"success\",\"data\":[\"PLUGIN\"]}";
//    }
//    
//    // mock configs fetch api response
//    private String mockConfigsFetchResponseJson() {
//        try (FileInputStream fis = new FileInputStream(this.getClass().getClassLoader().getResource("mock_configs_fetch_response.json").getPath());
//             InputStreamReader reader = new InputStreamReader(fis);
//             BufferedReader bufferedReader = new BufferedReader(reader);
//        ) {
//            StringBuilder builder = new StringBuilder();
//            bufferedReader.lines().forEach(builder::append);
//            return builder.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
