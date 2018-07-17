package com.bpwizard.myresources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonCustomizations {

    @Bean
    public Module myResourcesModules() {
        return new MyResourcesModules();
    }

    public static class MyResourcesModules extends SimpleModule {
		private static final long serialVersionUID = -7594652583222443715L;

		public MyResourcesModules() {
            addSerializer(DateTime.class, new DateTimeSerializer());
        }
    }

    public static class DateTimeSerializer extends StdSerializer<DateTime> {

        private static final long serialVersionUID = 3738435356887635382L;

		protected DateTimeSerializer() {
            super(DateTime.class);
        }

        @Override
        public void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(ISODateTimeFormat.dateTime().withZoneUTC().print(value));
            }
        }
    }

}
