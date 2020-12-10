package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * PluginData.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginData implements Serializable {

	private static final long serialVersionUID = -8830539796029898485L;

	private String id;

    private String name;

    private String config;

    private Integer role;

    private Boolean enabled;

}
