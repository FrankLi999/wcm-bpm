package com.bpwizard.gateway.metrics.prometheus.impl.collector;

import java.util.LinkedList;
import java.util.Map;

/**
 * The interface M bean receiver.
 */
public interface MBeanReceiver {
    
    /**
     * Record bean.
     *
     * @param domain          the domain
     * @param beanProperties  the bean properties
     * @param attrKeys        the attr keys
     * @param attrName        the attr name
     * @param attrType        the attr type
     * @param attrDescription the attr description
     * @param value           the value
     */
    void recordBean(String domain, Map<String, String> beanProperties,
                    LinkedList<String> attrKeys, String attrName, String attrType,
                    String attrDescription, Object value);
}
