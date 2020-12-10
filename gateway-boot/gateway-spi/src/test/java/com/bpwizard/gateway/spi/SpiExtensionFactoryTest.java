package com.bpwizard.gateway.spi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.spi.fixture.MysqlSPI;

public final class SpiExtensionFactoryTest {

    @Test
    public void testNull() {
        SpiExtensionFactory spiExtensionFactory = new SpiExtensionFactory();
        Assertions.assertNull(spiExtensionFactory.getExtension("testNull", MysqlSPI.class));
    }
}
