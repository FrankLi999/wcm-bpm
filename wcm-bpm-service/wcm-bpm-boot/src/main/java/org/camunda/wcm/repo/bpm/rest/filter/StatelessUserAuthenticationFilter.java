package org.camunda.wcm.repo.bpm.rest.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
// import org.camunda.bpm.engine.rest.util.EngineUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class StatelessUserAuthenticationFilter implements Filter {
	private static final String rolePrefix = "ROLE_";
	private final ProcessEngine engine;
	
	public StatelessUserAuthenticationFilter(ProcessEngine engine) {
		this.engine = engine;
	}
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Current limitation: Only works for the default engine
        // ProcessEngine engine = EngineUtil.lookupProcessEngine("default");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        try {
            engine.getIdentityService().setAuthentication(username, getUserGroups(username));
            chain.doFilter(request, response);
        } finally {
            clearAuthentication(engine);
        }

    }

    @Override
    public void destroy() {

    }

    private void clearAuthentication(ProcessEngine engine) {
        engine.getIdentityService().clearAuthentication();
    }

    private List<String> getUserGroups(String userId){

        List<String> groupIds;

        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        groupIds = authentication.getAuthorities().stream()
                .map(res -> res.getAuthority())
                .map(res -> res.startsWith(rolePrefix) ? res.substring(rolePrefix.length()): res)
                .collect(Collectors.toList());

        return groupIds;
    }

}