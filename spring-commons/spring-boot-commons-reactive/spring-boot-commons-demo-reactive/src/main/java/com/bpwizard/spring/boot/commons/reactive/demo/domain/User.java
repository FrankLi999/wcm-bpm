package com.bpwizard.spring.boot.commons.reactive.demo.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bpwizard.spring.boot.commons.reactive.service.domain.AbstractMongoUser;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@TypeAlias("User")
@Document(collection = "usr")
public class User extends AbstractMongoUser<ObjectId> {
	
}
