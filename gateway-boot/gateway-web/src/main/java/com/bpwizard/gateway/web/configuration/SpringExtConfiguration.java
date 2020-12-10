package com.bpwizard.gateway.web.configuration;

import com.bpwizard.gateway.plugin.base.utils.SpringBeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * The type Spring ext configuration.
 */
public class SpringExtConfiguration {

    /**
     * Application context aware application context aware.
     *
     * @return the application context aware
     */
    @Bean
    public ApplicationContextAware applicationContextAware() {
        return new GatewayApplicationContextAware();
    }

    /**
     * The type Gateway application context aware.
     */
    public static class GatewayApplicationContextAware implements ApplicationContextAware {

        @Override
        public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
            SpringBeanUtils.getInstance().setCfgContext((ConfigurableApplicationContext) applicationContext);
        }
    }
}
