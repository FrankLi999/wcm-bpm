package com.bpwizard.config.domain;

import java.io.Serializable;

// import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PropertyId implements Serializable {
    private static final long serialVersionUID = -1L;
    private String application;
    private String profile;
    private String label;
    private String key;
}