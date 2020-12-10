package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * AppAuthDTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthData implements Serializable {

	private static final long serialVersionUID = -8754715457355654859L;

	private String appKey;

    private String appSecret;

    private Boolean enabled;

    private List<AuthParamData> paramDataList;

    private List<AuthPathData> pathDataList;
}
