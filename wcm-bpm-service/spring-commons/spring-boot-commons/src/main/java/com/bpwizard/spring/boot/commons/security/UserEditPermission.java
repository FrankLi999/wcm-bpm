package com.bpwizard.spring.boot.commons.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#user, 'edit')")
public @interface UserEditPermission {}