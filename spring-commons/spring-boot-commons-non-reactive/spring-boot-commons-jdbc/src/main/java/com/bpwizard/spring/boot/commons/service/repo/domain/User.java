package com.bpwizard.spring.boot.commons.service.repo.domain;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * TODO: config @EntityScan to rename to package
 * https://docs.spring.io/spring-boot/docs/2.1.9.RELEASE/reference/html/howto-data-access.html#howto-separate-entity-definitions-from-spring-configuration
 */
//@Entity
//@Table(name="usr", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter @Setter 
@NoArgsConstructor
public class User extends AbstractUser<Long> {
	
	private static final long serialVersionUID = 2862969164717685466L;

	public User(String email, String password, String name, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}