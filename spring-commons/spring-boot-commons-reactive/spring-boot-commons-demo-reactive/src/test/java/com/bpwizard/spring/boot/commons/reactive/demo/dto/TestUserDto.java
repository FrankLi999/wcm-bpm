package com.bpwizard.spring.boot.commons.reactive.demo.dto;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;

import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser.Tag;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TestUserDto {

	private ObjectId id;
	private String username;
	private String password;
	private Set<String> roles = new HashSet<String>();
	private Tag tag;
	
	private boolean unverified = false;
	private boolean blocked = false;
	private boolean admin = false;
	private boolean goodUser = false;
	private boolean goodAdmin = false;
}
