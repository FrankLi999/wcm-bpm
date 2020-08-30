package com.bpwizard.spring.boot.commons.service.repo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import com.bpwizard.spring.boot.commons.dto.AuthResponse;
import com.bpwizard.spring.boot.commons.dto.LoginRequest;
import com.bpwizard.spring.boot.commons.jpa.JpaUtils;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.SpringService;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;
import com.bpwizard.spring.boot.commons.service.repo.exception.ResourceNotFoundException;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

@Service
public class DefaultSpringService extends SpringService<User, Long> {
    @Autowired
    protected AuthenticationManager authenticationManager;
    
    // TODO: refine it
    public AuthResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDto currentUser = WebUtils.currentUser();
        String shortLivedAuthToken = blueTokenService.createToken(
				BlueTokenService.AUTH_AUDIENCE,
				currentUser.getUsername(),
				(long) properties.getJwt().getShortLivedMillis());
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginRequest.getEmail()));
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        return AuthResponse.fromUserAndToken(user, shortLivedAuthToken, properties.getJwt().getShortLivedMillis(), sessionId);
    }
    
    // TODO: refine it
    public AuthResponse getCurrentUser(SpringPrincipal userPrincipal) {
    	UserDto currentUser = WebUtils.currentUser();
        String shortLivedAuthToken = blueTokenService.createToken(
				BlueTokenService.AUTH_AUDIENCE,
				currentUser.getUsername(),
				(long) properties.getJwt().getShortLivedMillis());
   
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
    	User user = userRepository.findByEmail(userPrincipal.currentUser().getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.currentUser().getId()));
        return AuthResponse.fromUserAndToken(user, shortLivedAuthToken, properties.getJwt().getShortLivedMillis(), sessionId);
    }
    
	@Override
    public User newUser() {
        return new User();
    }

	@Override
    protected void updateUserFields(User user, User updatedUser, UserDto currentUser) {

        super.updateUserFields(user, updatedUser, currentUser);

        user.setName(updatedUser.getName());

        JpaUtils.afterCommit(() -> {
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