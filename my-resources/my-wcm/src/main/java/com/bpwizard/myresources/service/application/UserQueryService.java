package com.bpwizard.myresources.service.application;

import com.bpwizard.myresources.dto.UserData;
import com.bpwizard.myresources.service.data.jpa.UserReadService;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryService  {
    private UserReadService userReadService;

    public UserQueryService(UserReadService userReadService) {
        this.userReadService = userReadService;
    }

    public Optional<UserData> findById(Long id) {
        return Optional.ofNullable(userReadService.findById(id));
    }
}

