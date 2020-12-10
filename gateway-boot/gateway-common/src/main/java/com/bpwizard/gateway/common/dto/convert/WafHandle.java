package com.bpwizard.gateway.common.dto.convert;

import lombok.Data;

/**
 * this is waf plugin handle.
 */
@Data
public class WafHandle {

    /**
     * permission.
     */
    private String permission;

    /**
     * statusCode.
     */
    private String statusCode;

}
