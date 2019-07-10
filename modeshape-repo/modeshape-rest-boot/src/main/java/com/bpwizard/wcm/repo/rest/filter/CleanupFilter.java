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

import javax.jcr.Session;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.modeshape.jcr.api.Logger;
import org.modeshape.web.jcr.ModeshapeRequestContext;
import org.modeshape.web.jcr.WebLogger;

/**
 * {@link ContainerResponseFilter} implementation which will always close an active {@link Session} instance, if such an instance
 * has been opened during a request.
 * 
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class CleanupFilter implements Filter {

    private static final Logger LOGGER = WebLogger.getLogger(CleanupFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) 
    		throws IOException, ServletException {
        LOGGER.trace("Executing cleanup filter...");
        System.out.println(">>>>>>>>>>>>>>>>>>>>> CleanupFilter 1 ");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println(">>>>>>>>>>>>>>>>>>>>> CleanupFilter 2: " + ModeshapeRequestContext.get());
        //AbstractHandler.cleanupActiveSession();
        ModeshapeRequestContext.cleanup();
        System.out.println(">>>>>>>>>>>>>>>>>>>>> CleanupFilter 3 ");
    }
    
    @Override
    public void destroy() {
    	// Do nothing
    }
}
