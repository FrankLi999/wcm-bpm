package com.bpwizard.wcm.modeshape.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Spring Properties
 * 
 * @author Sanjay Patel
 */
@Validated
@ConfigurationProperties(prefix="bpw")
@Getter @Setter
public class SpringProperties {
	public static final String TOKEN_RESPONSE_HEADER_NAME = "Authorization";
    private static final Logger log = LogManager.getLogger(SpringProperties.class);
    public SpringProperties() {
		log.info("Created");
	}
    
    private final OAuth2 oauth2 = new OAuth2();

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }

    /**
	 * Client web application's base URL.
	 * Used in the verification link mailed to the users, etc.
	 */
    private String applicationUrl = "http://localhost:8080";
    
	/**
	 * The default URL to redirect to after
	 * a user logs in using OAuth2/OpenIDConnect
	 */
    private String oauth2AuthenticationSuccessUrl = "http://localhost:8080/social-login-success?token=";

    /**
	 * Recaptcha related properties
	 */
	private Recaptcha recaptcha = new Recaptcha();
	
    /**
	 * CORS related properties
	 */
	private Cors cors = new Cors();

    /**
	 * Properties related to the initial Admin user to be created
	 */
	private Admin admin = new Admin();
	
	
	/**
     * Any shared properties you want to pass to the 
     * client should begin with bpw.shared.
     */
	private Map<String, Object> shared;

	/**
	 * JWT token generation related properties
	 */
	private Jwt jwt;


	/**************************
	 * Static classes
	 *************************/

	/**
     * Recaptcha related properties
     */
	@Getter @Setter
	public static class Recaptcha {
    	
		/**
         * Google ReCaptcha Site Key
         */
    	private String sitekey;
    	        
        /**
         * Google ReCaptcha Secret Key
         */
    	private String secretkey;
    }
	
	
    /**
     * CORS configuration related properties
     */
	@Getter @Setter
	public static class Cors {
		
		/**
		 * Comma separated whitelisted URLs for CORS.
		 * Should contain the applicationURL at the minimum.
		 * Not providing this property would disable CORS configuration.
		 */
		private String[] allowedOrigins;
		
		/**
		 * Methods to be allowed, e.g. GET,POST,...
		 */
		private String[] allowedMethods = {"GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "PATCH"};
		
		/**
		 * Request headers to be allowed, e.g. content-type,accept,origin,x-requested-with,...
		 */
		private String[] allowedHeaders = {
				"Accept",
				"Accept-Encoding",
				"Accept-Language",
				"Cache-Control",
				"Connection",
				"Content-Length",
				"Content-Type",
				"Cookie",
				"Host",
				"Origin",
				"Pragma",
				"Referer",
				"User-Agent",
				"x-requested-with",
				HttpHeaders.AUTHORIZATION};
		
		/**
		 * Response headers that you want to expose to the client JavaScript programmer, e.g. BPW-Authorization.
		 * I don't think we need to mention here the headers that we don't want to access through JavaScript.
		 * Still, by default, we have provided most of the common headers.
		 *  
		 * <br>
		 * See <a href="http://stackoverflow.com/questions/25673089/why-is-access-control-expose-headers-needed#answer-25673446">
		 * here</a> to know why this could be needed.
		 */		
		private String[] exposedHeaders = {
				"Cache-Control",
				"Connection",
				"Content-Type",
				"Date",
				"Expires",
				"Pragma",
				"Server",
				"Set-Cookie",
				"Transfer-Encoding",
				"X-Content-Type-Options",
				"X-XSS-Protection",
				"X-Frame-Options",
				"X-Application-Context",
				TOKEN_RESPONSE_HEADER_NAME};
		
		/**
		 * CORS <code>maxAge</code> long property
		 */
		private long maxAge = 3600L;
    }

	
	/**
	 * Properties regarding the initial Admin user to be created
	 * 
	 * @author Sanjay Patel
	 */
	@Getter @Setter
	public static class Admin {
		
		/**
		 * Login ID of the initial Admin user to be created 
		 */
		private String username;
		
		/**
		 * Password of the initial Admin user to be created 
		 */		
		private String password;
	}
	
	/**
	 * Properties related to JWT token generation
	 * 
	 * @author Sanjay Patel
	 */
	@Getter @Setter
	public static class Jwt {
		
		/**
		 * Secret for signing JWT
		 */
		private String secret;
		
		/**
		 * Default expiration milliseconds
		 */
		private long expirationMillis = 864000000L; // 10 days
		
		/**
		 * Expiration milliseconds for short-lived tokens and cookies
		 */
		private int shortLivedMillis = 120000; // Two minutes
	}	
}
