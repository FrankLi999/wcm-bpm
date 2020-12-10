package com.bpwizard.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * The enum Data sync enum.
 */
@RequiredArgsConstructor
@Getter
public enum DataSyncEnum {
    
    /**
     * Http data sync enum.
     */
    HTTP("http"),

    /**
     * Websocket data sync enum.
     */
    WEBSOCKET("websocket"),;

    private final String name;
    
    /**
     * Acquire by name data sync enum.
     *
     * @param name the name
     * @return the data sync enum
     */
    public static DataSyncEnum acquireByName(final String name) {
        return Arrays.stream(DataSyncEnum.values())
                .filter(e -> e.getName().equals(name)).findFirst()
                .orElse(DataSyncEnum.HTTP);
    }
}
