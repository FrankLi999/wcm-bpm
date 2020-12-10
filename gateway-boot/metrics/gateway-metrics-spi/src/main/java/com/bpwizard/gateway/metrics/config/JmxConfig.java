package com.bpwizard.gateway.metrics.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.management.ObjectName;
import lombok.Data;

/**
 * The type Jmx config.
 */
@Data
public class JmxConfig implements Serializable {

	private static final long serialVersionUID = 8395997799585206925L;

	/**
     * The Start delay seconds.
     */
    private Integer startDelaySeconds = 0;
    
    /**
     * The Jmx url.
     */
    private String jmxUrl = "";
    
    /**
     * The Username.
     */
    private String username = "";
    
    /**
     * The Password.
     */
    private String password = "";
    
    /**
     * The Ssl.
     */
    private boolean ssl;
    
    /**
     * The Lowercase output name.
     */
    private boolean lowercaseOutputName;
    
    /**
     * The Lowercase output label names.
     */
    private boolean lowercaseOutputLabelNames;
    
    /**
     * The Whitelist object names.
     */
    private List<ObjectName> whitelistObjectNames = new ArrayList<>();
    
    /**
     * The Blacklist object names.
     */
    private List<ObjectName> blacklistObjectNames = new ArrayList<>();
    
    /**
     * The Rules.
     */
    private List<Rule> rules = new ArrayList<>();
    
    /**
     * The type Rule.
     */
    @Data
    public static class Rule {
        
        private Pattern pattern;
        
        /**
         * The Name.
         */
        private String name;
        
        /**
         * The Value.
         */
        private String value;
        
        /**
         * The Value factor.
         */
        private Double valueFactor = 1.0;
        
        /**
         * The Help.
         */
        private String help;
    
        /**
         * The Attr name snake case.
         */
        private boolean attrNameSnakeCase;
    
        /**
         * The Type.
         */
        private Type type = Type.UNTYPED;
    
        /**
         * The Label names.
         */
        private List<String> labelNames = new ArrayList<>();
    
        /**
         * The Label values.
         */
        private List<String> labelValues = new ArrayList<>();
    }
    
    /**
     * The enum Type.
     */
    public enum Type {
        /**
         * Counter type.
         */
        COUNTER,
        /**
         * Gauge type.
         */
        GAUGE,
        /**
         * Summary type.
         */
        SUMMARY,
        /**
         * Histogram type.
         */
        HISTOGRAM,
        /**
         * Untyped type.
         */
        UNTYPED,
    }
}
