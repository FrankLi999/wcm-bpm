package com.bpwizard.spring.boot.commons.demo.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="usr", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter @Setter 
@NoArgsConstructor
public class User extends AbstractUser<Long> {
	public User(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
}