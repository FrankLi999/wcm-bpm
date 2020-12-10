package com.bpwizard.gateway.spi;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.spi.fixture.JdbcSPI;
import com.bpwizard.gateway.spi.fixture.MysqlSPI;
import com.bpwizard.gateway.spi.fixture.NopSPI;

public final class ExtensionLoaderTest {

    @Test
    public void testSPI() {
        JdbcSPI jdbcSPI = ExtensionLoader.getExtensionLoader(JdbcSPI.class).getJoin("mysql");
        MatcherAssert.assertThat(jdbcSPI.getClass().getName(), CoreMatchers.is(MysqlSPI.class.getName()));
    }

    @Test
    public void testGetExtensionLoaderNotInterface() {
        try {
            ExtensionLoader.getExtensionLoader(ExtensionLoaderTest.class);
            Assertions.fail();
        } catch (IllegalArgumentException expected) {
        	MatcherAssert.assertThat(expected.getMessage(),
                    CoreMatchers.containsString("extension clazz (class com.bpwizard.gateway.spi.ExtensionLoaderTest) is not interface!"));
        }
    }

    @Test
    public void testGetExtensionLoaderNotSpiAnnotation() {
        try {
            ExtensionLoader.getExtensionLoader(NopSPI.class);
            Assertions.fail();
        } catch (IllegalArgumentException expected) {
        	MatcherAssert.assertThat(expected.getMessage(),
                    CoreMatchers.containsString("extension clazz (interface com.bpwizard.gateway.spi.fixture.NopSPI) without @interface com.bpwizard.gateway.spi.SPI Annotation"));
        }
    }
}
