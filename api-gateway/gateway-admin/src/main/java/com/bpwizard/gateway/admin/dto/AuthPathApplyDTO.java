package com.bpwizard.gateway.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Auth path apply dto.
 */
@Data
public class AuthPathApplyDTO implements Serializable {

	private static final long serialVersionUID = -3330024390424448360L;

	private String appName;

    private String path;
}
