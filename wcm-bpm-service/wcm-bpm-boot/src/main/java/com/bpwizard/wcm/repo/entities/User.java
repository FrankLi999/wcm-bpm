package com.bpwizard.wcm.repo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
// import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="usr", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter @Setter 
@NoArgsConstructor
public class User extends AbstractUser<Long> {
	
	public User(String email, String password, String name, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}