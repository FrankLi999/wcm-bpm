package com.bpwizard.spring.boot.commons.service.repo.service;

import java.util.Map;

import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.stereotype.Service;

import com.bpwizard.spring.boot.commons.jdbc.JdbcUtils;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.SpringService;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;

@Service
public class DefaultSpringService extends SpringService<User, Long> {
    
    @Override
    public User newUser() {
        return new User();
    }

	@Override
    protected void updateUserFields(User user, User updatedUser, UserDto currentUser) {

        super.updateUserFields(user, updatedUser, currentUser);

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
    public void fillAdditionalFields(String registrationId, User user, Map<String, Object> attributes) {
    	
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
}