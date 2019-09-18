package com.bpwizard.spring.boot.commons.webflux.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.github.fge.jsonpatch.JsonPatchException;

import reactor.core.publisher.Mono;

/**
 * Useful helper methods
 * 
 * @author Sanjay Patel
 */
public class ReactiveUtils {
	
	private static final Logger log = LogManager.getLogger(ReactiveUtils.class);
	
	private static Mono<Object> NOT_FOUND_MONO;
	
	@PostConstruct
	public void postConstruct() {
		NOT_FOUND_MONO = Mono.error(SpringExceptionUtils.NOT_FOUND_EXCEPTION);
	}
	
	/**
	 * Gets the current-user
	 */
	public static <ID extends Serializable> Mono<Optional<UserDto>> currentUser() {
		
		return ReactiveSecurityContextHolder.getContext()
			.map(SecurityUtils::currentUser)
			.map(user -> Optional.of(user))
			.defaultIfEmpty(Optional.empty());
	}	
		
	public static <T> Mono<T> notFoundMono() {
		return (Mono<T>) NOT_FOUND_MONO;
	}

	public static<T> T applyPatch(T originalObj, String patchString) {

		try {
			return SecurityUtils.applyPatch(originalObj, patchString);
		} catch (IOException | JsonPatchException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Fetches a cookie from the request
	 */
//	public static Optional<Cookie> fetchCookie(HttpServletRequest request, String name) {
//		
//		Cookie[] cookies = request.getCookies();
//	
//		if (cookies != null && cookies.length > 0)
//			for (int i = 0; i < cookies.length; i++)
//				if (cookies[i].getName().equals(name))
//					return Optional.of(cookies[i]);
//		
//		return Optional.empty();
//	}
//	
	/**
	 * Utility for deleting related cookies
	 */
//	public static void deleteCookies(HttpServletRequest request, HttpServletResponse response, String... cookiesToDelete) {
//		
//		Cookie[] cookies = request.getCookies();
//		if (cookies != null && cookies.length > 0) {
//			for (int i = 0; i < cookies.length; i++)
//				if (ArrayUtils.contains(cookiesToDelete, cookies[i].getName())) {					
//					cookies[i].setValue("");
//					cookies[i].setPath("/");
//					cookies[i].setMaxAge(0);
//					response.addCookie(cookies[i]);
//				}
//		}
//	}
}
