package com.bpwizard.camunda.identity.boot.plugin;

import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.Session;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bpwizard.wcm.repo.camunda.identity.service.SpringGroupService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringTenantService;
import com.bpwizard.wcm.repo.camunda.identity.service.SpringUserService;

@Service
public class SpringIdentityProviderSessionFactory implements SessionFactory {

    private final SpringUserService userService;

    private final SpringGroupService groupService;
    
    private final SpringTenantService tenantService;

    private final PasswordEncoder passwordEncoder;
    
    public SpringIdentityProviderSessionFactory(
    		SpringUserService userService, 
    		SpringGroupService groupService,
    		SpringTenantService tenantService,
    		PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.groupService = groupService;
        this.tenantService = tenantService;
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
        return new SpringIdentityProvider(userService, groupService, tenantService, passwordEncoder);
    }
}
