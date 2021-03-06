package com.bpwizard.spring.boot.commons.web.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;

public class WebUtils {

	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

	public WebUtils() {
		
		logger.info("Created");
	}

	/**
	 * Fetches a cookie from the request
	 */
	public static Optional<Cookie> fetchCookie(HttpServletRequest request, String name) {
		
		Cookie[] cookies = request.getCookies();
	
		if (cookies != null && cookies.length > 0)
			for (int i = 0; i < cookies.length; i++)
				if (cookies[i].getName().equals(name))
					return Optional.of(cookies[i]);
		
		return Optional.empty();
	}
	
	/**
	 * Utility for deleting related cookies
	 */
	public static void deleteCookies(HttpServletRequest request, HttpServletResponse response, String... cookiesToDelete) {
		
		Cookie[] cookies = request.getCookies();
		List<String> deletecookies = Arrays.asList(cookiesToDelete);
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++)
				if (deletecookies.contains(cookies[i].getName())) {				
					cookies[i].setValue("");
					cookies[i].setPath("/");
					cookies[i].setMaxAge(0);
					response.addCookie(cookies[i]);
				}
		}
	}

	/**
	 * Gets the current-user
	 */
	public static UserDto currentUser() {
		
		return SecurityUtils.currentUser(SecurityContextHolder.getContext());
	}
	
	public static String currentUserId() {
		UserDto currentUser = currentUser();
		return (currentUser == null) ? null : currentUser.getId(); 
	}

}
