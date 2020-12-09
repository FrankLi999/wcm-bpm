package com.bpwizard.spring.boot.commons.service.repo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.jdbc.JdbcUtils;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.SpringService;
import com.bpwizard.spring.boot.commons.service.domain.Role;
import com.bpwizard.spring.boot.commons.service.domain.Tenant;
import com.bpwizard.spring.boot.commons.service.domain.User;

@Service
public class DefaultSpringService extends SpringService<User<Long>, Long> {
    protected static final Logger logger = LoggerFactory.getLogger(DefaultSpringService.class);
    @Override
    public User<Long> newUser() {
        return new User<Long>();
    }

	@Override
    protected void updateUserFields(User<Long> user, User<Long> updatedUser, UserDto currentUser, Set<String> removedRoles, Set<String> newRoles) {

        super.updateUserFields(user, updatedUser, currentUser, removedRoles, newRoles);

        user.setName(updatedUser.getName());

        JdbcUtils.afterCommit(() -> {
            if (currentUser.getId().equals(user.getId().toString()))
                currentUser.setTag(user.toTag());
        });
    }
    
//    @Override
//    protected User createAdminUser() {
//    	
//    	User user = super.createAdminUser(); 
//    	user.setName(ADMIN_NAME);
//    	return user;
//    }    
    
    @Override
    public void fillAdditionalFields(String registrationId, User<Long> user, Map<String, Object> attributes) {
    	
    	String nameKey;
    	
    	switch (registrationId) {
    		
    	case "facebook":
    		nameKey = StandardClaimNames.NAME;
    		break;
    		
    	case "google":
			nameKey = StandardClaimNames.NAME;
			break;
			
		default:
			throw new UnsupportedOperationException("Fetching name from " + registrationId + " login not supprrted");
    	}
    	
    	user.setName((String) attributes.get(nameKey));
    }

	@Override
	public Long toId(String id) {
		
		return Long.valueOf(id);
	}
	
	/**
	 * Creates the initial Admin user.
	 * Override this if needed.
	 */
	protected User<Long> createUser(SpringProperties.User user, List<Role> roles, List<Tenant> tenants) {
    	logger.info("Creating the initial user: " + user.getUsername());

    	// create the user
    	User<Long> newUser = new User<Long>();
    	newUser.setName(user.getUsername());
    	newUser.setEmail(user.getEmail());
    	if (StringUtils.hasText(user.getFirstName())) {
    		newUser.setFirstName(user.getFirstName());
    	}
    	if (StringUtils.hasText(user.getLastName())) {
    		newUser.setLastName(user.getLastName());
    	}
    	newUser.setPassword(passwordEncoder.encode(
				user.getPassword()));
    	if (!ObjectUtils.isEmpty(roles)) {
    		newUser.setRoles(roles.stream().map(Role::getName).collect(Collectors.toSet()));

    	}
    	if (!ObjectUtils.isEmpty(tenants)) {
    		newUser.setTenants(tenants.stream().map(Tenant::getName).collect(Collectors.toSet()));
    	}
    	
    	newUser = userService.create(newUser);	

		return newUser;
	}
}