package com.bpwizard.spring.boot.commons.reactive.service.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.CredentialsContainer;

import com.bpwizard.spring.boot.commons.domain.SpringUser;
import com.bpwizard.spring.boot.commons.mongo.AbstractDocument;
import com.bpwizard.spring.boot.commons.reactive.service.validation.UniqueEmail;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.bpwizard.spring.boot.commons.validation.Password;
import com.bpwizard.spring.boot.commons.vlidation.Captcha;
import com.bpwizard.wcm.repo.secureity.oauth2.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
public abstract class AbstractMongoUser
	<ID extends Serializable>
	extends AbstractDocument<ID> implements CredentialsContainer, SpringUser<ID> {

	public static final int NAME_MIN = 1;
    public static final int NAME_MAX = 50;

    @Getter @Setter @ToString
	public static class Tag implements Serializable {
		
		private static final long serialVersionUID = -2129078111926834670L;
		private String name;
	}

	@JsonView(UserUtils.SignupInput.class)
	@NotBlank(message = "{blank.name}", groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    @Size(min=NAME_MIN, max=NAME_MAX, groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    private String name;
	
	// email
	@JsonView(UserUtils.SignupInput.class)
	@UniqueEmail(groups = {UserUtils.SignUpValidation.class})
	@Indexed(unique = true)
	protected String email;
	
	// password
	@JsonView(UserUtils.SignupInput.class)
	@Password(groups = {UserUtils.SignUpValidation.class, UserUtils.ChangeEmailValidation.class})
	protected String password;
	
	protected Set<String> roleNames = new HashSet<>();
	
	@Indexed(unique = true, sparse = true)
	protected String newEmail;
	
	@JsonIgnore
	protected long credentialsUpdatedMillis = System.currentTimeMillis();
	
	// holds reCAPTCHA response while signing up
	@Transient
	@JsonView(UserUtils.SignupInput.class)
	@Captcha
	private String captchaResponse;
	
	private String imageUrl;
    // @Column(nullable = false)
    private Boolean emailVerified = false;

    // @NotNull
    private String provider = AuthProvider.google.name();

    private String providerId;
    
	public final boolean hasRole(String role) {
		return roleNames.contains(role);
	}	
	
	/**
	 * Called by spring security permission evaluator
	 * to check whether the current-user has the given permission
	 * on this entity. 
	 */
	@Override
	public boolean hasPermission(UserDto currentUser, String permission) {		
		return UserUtils.hasPermission(getId(), currentUser, permission);		
	}

	
	/**
	 * A convenient toString method
	 */
	@Override
	public String toString() {
		return "AbstractUser [email=" + email + ", roleNames=" + roleNames + "]";
	}


	/**
	 * Makes a User DTO
	 */
	public UserDto toUserDto() {
		UserDto userDto = new UserDto();
		
		userDto.setId(getId().toString());
		userDto.setUsername(email);
		userDto.setPassword(password);
		userDto.setEmail(email);
		userDto.setEmailVerified(this.emailVerified);
		userDto.setImageUrl(this.imageUrl);
		userDto.setProvider(this.provider);
		userDto.setProviderId(this.providerId);
		
		
		// roles would be org.hibernate.collection.internal.PersistentSet,
		// which is not in another microservices not having Hibernate.
		// So, let's convert it to HashSet
		userDto.setRoles(new HashSet<String>(roleNames));
		
		userDto.setTag(toTag());
		
		userDto.initialize();

		return userDto;
	}

	/**
	 * Override this to supply any additional fields to the user DTO,
	 * e.g. name
	 */
	public Tag toTag() {
		
		Tag tag = new Tag();
		tag.setName(name);
		return tag;
	}

	@Override
	public void eraseCredentials() {
		password = null;
	}
}
