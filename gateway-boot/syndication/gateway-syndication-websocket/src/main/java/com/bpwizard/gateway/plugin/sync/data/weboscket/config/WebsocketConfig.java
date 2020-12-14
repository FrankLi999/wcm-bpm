package com.bpwizard.gateway.plugin.sync.data.weboscket.config;

import lombok.Data;

@Data
public class WebsocketConfig {
    
    /**
     * if have more gateway admin url,please config like this.
     * 127.0.0.1:8888,127.0.0.1:8889
     */
    private String urls;
}
