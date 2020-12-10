package com.bpwizard.gateway.common.constant;

/**
 * RedisKeyConstants.
 */
public final class RedisKeyConstants implements Constants {

    /**
     * The constant PLUGIN.
     */
    public static final String PLUGIN = "plugin";

    /**
     * The constant SELECTOR.
     */
    public static final String SELECTOR = "selector";

    /**
     * The constant RULE.
     */
    public static final String RULE = "rule";

    private static final String PLUGIN_INFO = ":info";

    private static final String PLUGIN_SELECTOR = ":selector";

    /**
     * this is a function.
     *
     * @param pluginName pluginName
     * @return java.lang.String string
     */
    public static String pluginInfoKey(final String pluginName) {
        return String.join("", pluginName, PLUGIN_INFO);

    }

    /**
     * this is a function.
     *
     * @param pluginName pluginName
     * @return java.lang.String string
     */
    public static String pluginSelectorKey(final String pluginName) {
        return String.join("", pluginName, PLUGIN_SELECTOR);

    }

}
