package com.bpwizard.wcm.repo.syndication;
import com.bpwizard.spring.boot.commons.SpringProperties;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.apache.camel.model.rest.RestParamType;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class SyndicationRouter extends RouteBuilder {

    @Autowired
    private Environment env;
    
    @Value("${camel.component.servlet.mapping.context-path}")
    private String contextPath;
    
    @Autowired
    private SpringProperties springProperties;

    @Override
    public void configure() {
        // this can also be configured in application.properties
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .enableCORS(true)
            .port(env.getProperty("server.port", "18090"))
            .contextPath(contextPath.substring(0, contextPath.length() - 2))
            // turn on openapi api-doc
            .apiContextPath("/api-doc")
            .apiProperty("api.title", springProperties.getSwagger().getAppInfo().getTitle())
            .apiProperty("api.version", springProperties.getSwagger().getAppInfo().getVersion());
        
    	rest("/syndication").description("Syndication service")
	        	.consumes("application/json")
	        	.produces("application/json")
        	.post().description("Start a syndication").type(Syndication.class)
	        	.param().name("id").type(RestParamType.path).description("Syndicator ID").dataType("long").endParam()    
	            .param().name("body").type(RestParamType.body).description("The syndication model").endParam()
	            // .responseMessage().code(204).message("Syndicated successfully").endResponseMessage()
	            .to("direct:syndication");
	            
	     from("direct:syndication").to("bean:syndicationService?method=syndicate")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
            .setBody(constant("{\"status\": \"ok\"}"));
    }

}
