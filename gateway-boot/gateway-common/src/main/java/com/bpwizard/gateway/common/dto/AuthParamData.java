package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Auth param data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthParamData implements Serializable {

	private static final long serialVersionUID = -4871838599732116348L;

	private String appName;

    private String appParam;


}
