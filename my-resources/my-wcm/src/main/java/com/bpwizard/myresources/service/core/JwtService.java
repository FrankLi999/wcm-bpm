package com.bpwizard.myresources.service.core;

import org.springframework.stereotype.Service;

import com.bpwizard.myresources.resource.user.User;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(User user);

    Optional<Long> getSubFromToken(String token);
}
