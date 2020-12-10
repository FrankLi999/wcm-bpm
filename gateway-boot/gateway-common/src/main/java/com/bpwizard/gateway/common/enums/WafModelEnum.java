package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The enum Waf model enum.
 */
@RequiredArgsConstructor
@Getter
public enum WafModelEnum {
    
    /**
     * BLACK waf  model enum.
     */
    BLACK("black"),
    
    /**
     * Mixed waf model enum.
     */
    MIXED("mixed");
    
    private final String name;
    
}
