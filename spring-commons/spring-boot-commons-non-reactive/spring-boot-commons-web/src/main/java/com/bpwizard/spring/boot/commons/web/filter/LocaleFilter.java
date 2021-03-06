package com.bpwizard.spring.boot.commons.web.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringLocaleHolder;

public class LocaleFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(LocaleFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) 
    		throws IOException, ServletException {
    	if (logger.isDebugEnabled()) {
    		logger.debug("Entering");
        }
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String langHeader = request.getHeader("lang");
		if (StringUtils.hasText(langHeader)) {
			Locale locale = StringUtils.parseLocale(request.getHeader("lang"));
			SpringLocaleHolder.setLocale(locale);
		}
    	filterChain.doFilter(servletRequest, servletResponse);
    	if (logger.isDebugEnabled()) {
    		logger.debug("Exiting");
        }
    }
    
    @Override
    public void destroy() {
    	// Do nothing
    }
}
