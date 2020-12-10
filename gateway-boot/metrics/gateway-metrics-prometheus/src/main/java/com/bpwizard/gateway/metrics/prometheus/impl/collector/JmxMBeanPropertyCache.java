package com.bpwizard.gateway.metrics.prometheus.impl.collector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.ObjectName;

/**
 * This object stores a mapping of mBean objectNames to mBean key property lists. The main purpose of it is to reduce
 * the frequency with which we invoke PROPERTY_PATTERN when discovering mBeans.
 */
public class JmxMBeanPropertyCache {
    
    private static final Pattern PROPERTY_PATTERN = Pattern.compile(
            "([^,=:*?]+)"
                    + "="
                    + "("
                    + "\""
                    + "(?:"
                    + "[^\\\\\"]*"
                    + "(?:\\\\.)?"
                    + ")*"
                    + "\""
                    + "|"
                    + "[^,=:\"]*"
                    + ")");
    
    // Implement a version of ObjectName.getKeyPropertyList that returns the
    // properties in the ordered they were added (the ObjectName stores them
    // in the order they were added).
    private final Map<ObjectName, Map<String, String>> keyPropertiesPerBean;
    
    /**
     * Instantiates a new Jmx m bean property cache.
     */
    public JmxMBeanPropertyCache() {
        this.keyPropertiesPerBean = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets key property list.
     *
     * @param mbeanName the mbean name
     * @return the key property list
     */
    public Map<String, String> getKeyPropertyList(final ObjectName mbeanName) {
        Map<String, String> keyProperties = keyPropertiesPerBean.get(mbeanName);
        if (keyProperties == null) {
            keyProperties = new LinkedHashMap<>();
            String properties = mbeanName.getKeyPropertyListString();
            Matcher match = PROPERTY_PATTERN.matcher(properties);
            while (match.lookingAt()) {
                keyProperties.put(match.group(1), match.group(2));
                properties = properties.substring(match.end());
                if (properties.startsWith(",")) {
                    properties = properties.substring(1);
                }
                match.reset(properties);
            }
            keyPropertiesPerBean.put(mbeanName, keyProperties);
        }
        return keyProperties;
    }
    
    /**
     * Only keep m beans.
     *
     * @param latestBeans the latest beans
     */
    public void onlyKeepMBeans(final Set<ObjectName> latestBeans) {
        for (ObjectName prevName : keyPropertiesPerBean.keySet()) {
            if (!latestBeans.contains(prevName)) {
                keyPropertiesPerBean.remove(prevName);
            }
        }
    }
}
