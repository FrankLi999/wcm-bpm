package com.bpwizard.gateway.spi;

/**
 * The interface Extension factory.
 */
@SPI("spi")
public interface ExtensionFactory {
    
    /**
     * Gets Extension.
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param clazz the clazz
     * @return the extenstion
     */
    <T> T getExtension(String key, Class<T> clazz);
}
