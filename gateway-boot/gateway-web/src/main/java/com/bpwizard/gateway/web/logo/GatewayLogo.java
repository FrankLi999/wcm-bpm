package com.bpwizard.gateway.web.logo;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import com.bpwizard.gateway.common.constant.Constants;
import com.bpwizard.gateway.common.utils.VersionUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * the gateway logo.
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER + 1)
@Slf4j
public class GatewayLogo implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String GATEWAY_LOGO = "\n"
            + "                  _  \n"
            + "                  | | \n"
            + " ___  ___   _   _ | | \n"
            + "/ __|/ _ \\ | | | | |\n"
            + "\\__ \\ (_) | |_| | |\n"
            + "|___/\\___/ \\__,_|_|\n"
            + "                    \n"
            + "                   \n";

    private volatile AtomicBoolean alreadyLog = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        if (!alreadyLog.compareAndSet(false, true)) {
            return;
        }
        log.info(buildBannerText());
    }

    private String buildBannerText() {
        return Constants.LINE_SEPARATOR
                + Constants.LINE_SEPARATOR
                + GATEWAY_LOGO
                + Constants.LINE_SEPARATOR
                + " :: Gateway :: (v" + VersionUtils.getVersion(getClass(), "0.0.1-SNAPSHOT") + ")"
                + Constants.LINE_SEPARATOR;
    }

}
