package com.bpwizard.spring.boot.commons.reactive.demo.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.bpwizard.spring.boot.commons.reactive.demo.domain.User;
import com.bpwizard.spring.boot.commons.reactive.service.SpringReactiveService;
import com.bpwizard.spring.boot.commons.security.UserDto;

@Service
public class MyService extends SpringReactiveService<User, ObjectId> {

	public static final String ADMIN_NAME = "Administrator";

	@Override
	public User newUser() {
		return new User();
	}

    @Override
    protected User createAdminUser() {
    	
    	User user = super.createAdminUser(); 
    	user.setName(ADMIN_NAME);
    	return user;
    }
    
	@Override
    protected void updateUserFields(User user, User updatedUser, UserDto currentUser) {

        super.updateUserFields(user, updatedUser, currentUser);
        user.setName(updatedUser.getName());
    }

	@Override
	protected ObjectId toId(String id) {
		return new ObjectId(id);
	}
}