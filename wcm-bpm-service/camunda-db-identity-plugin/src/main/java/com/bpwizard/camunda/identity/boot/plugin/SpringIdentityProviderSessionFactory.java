package com.bpwizard.camunda.identity.boot.plugin;

import com.bpwizard.wcm.repo.camunda.identity.service.SpringGroupService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringUserService;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.Session;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by ashlah on 28/07/17.
 */
@Service
public class SpringIdentityProviderSessionFactory implements SessionFactory {

    private final SpringUserService userService;

    private final SpringGroupService groupService;

    private final PasswordEncoder passwordEncoder;
    
    public SpringIdentityProviderSessionFactory(
    		SpringUserService userService, 
    		SpringGroupService groupService,
    		PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.groupService = groupService;
        this.passwordEncoder = passwordEncoder;
    }

//    @Autowired
//    private CustomIdentityProvider customIdentityProvider;

    @Override
    public Class<?> getSessionType() {
        return ReadOnlyIdentityProvider.class;
    }

    @Override
    public Session openSession() {
        return new SpringIdentityProvider(userService, groupService, passwordEncoder);
    }
}
