/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bpwizard.wcm.repo.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.modeshape.jcr.api.Logger;
import org.modeshape.web.jcr.WebLogger;

/**
 * REST Easy {@link ContainerRequestFilter} which will print out various logging information in DEBUG mode.
 * 
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class LoggingFilter implements Filter {

    private static final Logger LOGGER = WebLogger.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) 
    		throws IOException, ServletException {
    	if (LOGGER.isDebugEnabled()) {
    		HttpServletRequest request = (HttpServletRequest) servletRequest;
            LOGGER.debug("Received request: {0}", request.getRequestURI());
            LOGGER.debug("Executing method: {0}", request.getMethod());
        }
    	filterChain.doFilter(servletRequest, servletResponse);
    }
    
    @Override
    public void destroy() {
    	// Do nothing
    }
}
