package com.bpwizard.myresources.service.core;

import org.springframework.stereotype.Service;

import com.bpwizard.myresources.resource.user.EncryptService;

@Service
public class NaiveEncryptService implements EncryptService {
    @Override
    public String encrypt(String password) {
        return password;
    }

    @Override
    public boolean check(String checkPassword, String realPassword) {
        return checkPassword.equals(realPassword);
    }
}
