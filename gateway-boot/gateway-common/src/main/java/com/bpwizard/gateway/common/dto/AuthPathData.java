package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Auth path data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthPathData implements Serializable {

	private static final long serialVersionUID = -946762042049318321L;

	private String appName;

    private String path;

    private Boolean enabled;
}
