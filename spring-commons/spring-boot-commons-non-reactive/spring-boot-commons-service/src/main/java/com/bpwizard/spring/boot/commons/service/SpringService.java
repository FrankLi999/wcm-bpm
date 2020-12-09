package com.bpwizard.spring.boot.commons.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.bpwizard.spring.boot.commons.AbstractSpringService;
import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.domain.ChangePasswordForm;
import com.bpwizard.spring.boot.commons.domain.ResetPasswordForm;
import com.bpwizard.spring.boot.commons.exceptions.util.ExceptionUtils;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.jdbc.JdbcUtils;
import com.bpwizard.spring.boot.commons.mail.MailSender;
import com.bpwizard.spring.boot.commons.mail.SpringMailData;
import com.bpwizard.spring.boot.commons.security.AuthenticationRequest;
import com.bpwizard.spring.boot.commons.security.JSONWebEncryptionService;
import com.bpwizard.spring.boot.commons.security.JSONWebSignatureService;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.security.UserEditPermission;
import com.bpwizard.spring.boot.commons.service.domain.Role;
import com.bpwizard.spring.boot.commons.service.domain.RoleService;
import com.bpwizard.spring.boot.commons.service.domain.Tenant;
import com.bpwizard.spring.boot.commons.service.domain.TenantService;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.domain.UserService;
import com.bpwizard.spring.boot.commons.service.util.ServiceUtils;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.nimbusds.jwt.JWTClaimsSet;

@Validated
@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public abstract class SpringService
	<U extends User<ID>, ID extends Serializable>
    extends AbstractSpringService<U, ID> {

    protected static final Logger logger = LoggerFactory.getLogger(SpringService.class);
    
	protected UserService<U, ID> userService;
	protected UserDetailsService userDetailsService;
	protected RoleService<Role, Long> roleService;
	protected TenantService<Tenant, Long> tenantService;
	protected Map<String, Role> preloadedRoles;
    protected AuthenticationManager authenticationManager;
	@Autowired
	public void createSpringService(SpringProperties properties,
			PasswordEncoder passwordEncoder,
			MailSender<?> mailSender,
			UserService<U, ID> userService,
			UserDetailsService userDetailsService,
			JSONWebSignatureService jwsTokenService,
			JSONWebEncryptionService jweTokenService,
			RoleService<Role, Long> roleService,
			TenantService<Tenant, Long> tenantService,
			Map<String, Role> preloadedRoles,
			AuthenticationManager authenticationManager) {
		
		this.properties = properties;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
		this.userService = userService;
		this.userDetailsService = userDetailsService;
		this.jwsTokenService = jwsTokenService;
		this.jweTokenService = jweTokenService;
		this.roleService = roleService;
		this.tenantService = tenantService;
		this.preloadedRoles = preloadedRoles;
		this.authenticationManager = authenticationManager;
		
		logger.info("Created");
	}

	
	/**
     * This method is called after the application is ready.
     * Needs to be public - otherwise Spring screams.
     * 
     * @param event
     */
//    @EventListener
//    public void afterApplicationReady(ApplicationReadyEvent event) {
//    	
//    	logger.info("Starting up Spring Commons ...");
//    	onStartup(); // delegate to onStartup() -- todo - it bypassed spring annotations
//    	logger.info("Spring Commons started");	
//    }

    
	/**
	 * Creates the initial Admin user, if not found.
	 * Override this method if needed.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false, timeout=60)
    public void onStartup() {
    	if (null != properties.getUser() && properties.getUser().length > 0) {
			try {
				
				// Check if the user already exists
				userDetailsService
					.loadUserByUsername(properties.getUser()[0].getUsername());
				
			} catch (UsernameNotFoundException e) {
				//TODO: batch mode		
				// Doesn't exist. So, create it.
	//			Tenant tenant = new Tenant();
	//			tenant.setName("default");
	//			tenantService.save(tenant);
				// Set<String> tenantNames = null;
				List<Tenant> tenants = null;
				String[] roleNames = properties.getRolename();
//				if (tenantNames != null) { 
//					for (String tenantName: tenantNames) {
//						Tenant tenant = new Tenant();
//						tenant = tenantService.save(tenant);
//						tenants.add(tenant);
//						
//					}
//				}
				List<Role> roles = new ArrayList<>();
				if (null != roleNames) {
					for (String roleName: roleNames) {
						Role role = createRole(roleName, tenants);
						this.preloadedRoles.put(roleName, role);
						roles.add(role);
					}
				}
				
				SpringProperties.User[] users = properties.getUser();
				for (SpringProperties.User user: users) {
					createUser(user, roles, tenants);
				}
			}
    	}
	}

	protected Role createRole(String roleName, List<Tenant> tenants) {
		Role role = new Role();
		role.setName(roleName);
		role.setTenants(tenants.stream().map(Tenant::getName).collect(Collectors.toSet()));
		role = roleService.save(role);
    	return role;
	}

	public abstract U newUser();

	protected abstract U createUser(SpringProperties.User user, List<Role> roles, List<Tenant> tenants);
	
//	/**
//	 * Creates the initial Admin user.
//	 * Override this if needed.
//	 */
//    protected U createAdminUser() {
//		
//    	// fetch data about the user to be created
//    	Admin initialAdmin = properties.getAdmin();
//    	
//    	logger.info("Creating the first admin user: " + initialAdmin.getUsername());
//
//    	// create the user
//    	U user = newUser();
//    	user.setEmail(initialAdmin.getUsername());
//		user.setPassword(passwordEncoder.encode(
//			properties.getAdmin().getPassword()));
//		user.getRoles().add(UserUtils.Role.ADMIN);
//		
//		return user;
//	}

    
	/**
	 * Creates a new user object. Must be overridden in the
	 * subclass, like this:
	 * 
	 * <pre>
	 * public User newUser() {
	 *    return new User();
	 * }
	 * </pre>
	 */
    // public abstract U newUser();

    public Map<String, Role> getPreloadedRoles() {
    	return this.preloadedRoles;
    }

	/**
	 * Returns the context data to be sent to the client,
	 * i.e. <code>reCaptchaSiteKey</code> and all the properties
	 * prefixed with <code>bpw.shared</code>.
	 * 
	 * To send custom properties, put those in your application
	 * properties in the format <em>bpw.shared.fooBar</em>.
	 * 
	 * If a user is logged in, it also returns the user data
	 * and a new authorization token. If expirationMillis is not provided,
	 * the expiration of the new token is set to the default.
	 *
	 * Override this method if needed.
	 */
	public Map<String, Object> getContext(Optional<Long> expirationMillis, HttpServletResponse response) {
		
		logger.debug("Getting context ...");

		// make the context
//		Map<String, Object> sharedProperties = new HashMap<String, Object>(2);
//		sharedProperties.put("reCaptchaSiteKey", properties.getRecaptcha().getSitekey());
//		sharedProperties.put("shared", properties.getShared());
//		
//		UserDto currentUser = WebUtils.currentUser();
//		if (currentUser != null)
//			addAuthHeader(response, currentUser.getUsername(),
//				expirationMillis.orElse(properties.getJwt().getExpirationMillis()));
//		
//		return SecurityUtils.mapOf(
//				"context", sharedProperties,
//				"user", WebUtils.currentUser());
		
		Map<String, Object> context = buildContext();
		
		UserDto currentUser = WebUtils.currentUser();
		if (currentUser != null) {
			addAuthHeader(response, currentUser.getUsername(),
				expirationMillis.orElse(properties.getJwt().getExpirationMillis()));
			context.put("user", currentUser);
		}
		
		return context;	
	}
	
	// TODO: refine it
    public String authenticateUser(AuthenticationRequest authenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		authenticationRequest.getEmail(),
                		authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return this.getShortLivedAuthToken(WebUtils.currentUser().getUsername());
    }
    
    /**
	 * Signs up a user.
	 */
	//TODO - validation, capcha
	// @Validated(UserUtils.SignUpValidation.class)
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void signup(@Valid U user) {
		
		logger.debug("Signing up user: " + user);

		initUser(user); // sets right all fields of the user
		userService.create(user);

		// if successfully committed
		JdbcUtils.afterCommit(() -> {
			ServiceUtils.login(user); // log the user in
			logger.debug("Signed up user: " + user);
		});
	}
	
	
	/**
	 * Initializes the user based on the input data,
	 * e.g. encrypts the password
	 */
	protected void initUser(U user) {
		
		logger.debug("Initializing user: " + user);

		user.setPassword(passwordEncoder.encode(user.getPassword())); // encode the password
		Set<String> removedRoles = new HashSet<>();
		Set<String> newRoles = new HashSet<>();
		makeUnverified(user, removedRoles, newRoles); // make the user unverified
	}

	
	/**
	 * Makes a user unverified
	 */
	protected void makeUnverified(U user, Set<String> removedRoles, Set<String> newRoles) {
		newRoles.add(this.preloadedRoles.get(UserUtils.Role.UNVERIFIED).getName());
		user.getRoles().add(this.preloadedRoles.get(UserUtils.Role.UNVERIFIED).getName());
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		JdbcUtils.afterCommit(() -> sendVerificationMail(user)); // send a verification mail to the user
	}
	
	
	/**
	 * Resends verification mail to the user.
	 */
	@UserEditPermission
	public void resendVerificationMail(U user) {

		// The user must exist
		SpringExceptionUtils.ensureFound(user);
		
		// must be unverified
		SpringExceptionUtils.validate(user.hasRole(UserUtils.Role.UNVERIFIED),
				"com.bpwizard.spring.alreadyVerified").go();	

		// send the verification mail
		sendVerificationMail(user);
	}

	
	/**
	 * Fetches a user by email
	 */
	public U fetchUserByEmail(@Valid @Email @NotBlank String email) {
		
		logger.debug("Fetching user by email: " + email);
		return processUser(userService.findByEmail(email).orElse(null));
	}

	
	/**
	 * Returns a non-null, processed user for the client.
	 */
	public U processUser(U user) {
		
		logger.debug("Fetching user: " + user);

		// ensure that the user exists
		SpringExceptionUtils.ensureFound(user);
		
		// hide confidential fields
		hideConfidentialFields(user);
		
		return user;
	}
	
	
	/**
	 * Verifies the email id of current-user
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void verifyUser(ID userId, String verificationCode) {
		
		logger.debug("Verifying user ...");

		U user = userService.findById(userId).orElseThrow(SpringExceptionUtils.notFoundSupplier());
		
		// ensure that he is unverified
		SpringExceptionUtils.validate(user.hasRole(UserUtils.Role.UNVERIFIED),
				"com.bpwizard.spring.alreadyVerified").go();	
		
		JWTClaimsSet claims = jweTokenService.parseToken(verificationCode,
				JSONWebEncryptionService.VERIFY_AUDIENCE, user.getCredentialsUpdatedMillis());
		
		SecurityUtils.ensureAuthority(
				claims.getSubject().equals(user.getId().toString()) &&
				claims.getClaim("email").equals(user.getEmail()),
				"com.bpwizard.spring.wrong.verificationCode");
		
		user.getRoles().remove(UserUtils.Role.UNVERIFIED); // make him verified
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		Set<String> removedRoles = new HashSet<>();
		removedRoles.add(UserUtils.Role.UNVERIFIED);
		userService.save(user, removedRoles, null);
		
		// after successful commit,
		JdbcUtils.afterCommit(() -> {
			
			// Re-login the user, so that the UNVERIFIED role is removed
			ServiceUtils.login(user);
			logger.debug("Re-logged-in the user for removing UNVERIFIED role.");		
		});
		
		logger.debug("Verified user: " + user);		
	}

	
	/**
	 * Forgot password.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void forgotPassword(@Valid @Email @NotBlank String email) {
		
		logger.debug("Processing forgot password for email: " + email);
		
		// fetch the user record from database
		U user = userService.findByEmail(email)
				.orElseThrow(SpringExceptionUtils.notFoundSupplier());

		mailForgotPasswordLink(user);
	}
	

	/**
	 * Resets the password.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void resetPassword(@Valid ResetPasswordForm form) {
		
		logger.debug("Resetting password ...");

		JWTClaimsSet claims = jweTokenService.parseToken(form.getCode(),
				JSONWebEncryptionService.FORGOT_PASSWORD_AUDIENCE);
		
		String email = claims.getSubject();
		
		// fetch the user
		U user = userService.findByEmail(email).orElseThrow(SpringExceptionUtils.notFoundSupplier());
		ServiceUtils.ensureCredentialsUpToDate(claims, user);
		
		// sets the password
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		//user.setForgotPasswordCode(null);
		
		userService.save(user, null, null);
		
		// after successful commit,
		JdbcUtils.afterCommit(() -> {
			
			// Login the user
			ServiceUtils.login(user);
		});
		
		logger.debug("Password reset.");		
	}

	
	/**
	 * Updates a user with the given data.
	 */
	@UserEditPermission
	@Validated(UserUtils.UpdateValidation.class)
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public UserDto updateUser(U user, @Valid U updatedUser) {
		
		logger.debug("Updating user: " + user);

		// checks
		JdbcUtils.ensureCorrectVersion(user, updatedUser);

		// delegates to updateUserFields
		Set<String> removedRoles = new HashSet<>();
		Set<String> newRoles = new HashSet<>();
		updateUserFields(user, updatedUser, WebUtils.currentUser(), removedRoles, newRoles);
		userService.save(user, removedRoles, newRoles);
		
		logger.debug("Updated user: " + user);
		
		UserDto userDto = user.toUserDto();
		userDto.setPassword(null);
		return userDto;
	}
	
	
	/**
	 * Changes the password.
	 */
	@UserEditPermission
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public String changePassword(U user, @Valid ChangePasswordForm changePasswordForm) {
		
		logger.debug("Changing password for user: " + user);
		
		// Get the old password of the logged in user (logged in user may be an ADMIN)
		UserDto currentUser = WebUtils.currentUser();
		U loggedIn = userService.findById(toId(currentUser.getId())).get();
		String oldPassword = loggedIn.getPassword();

		// checks
		SpringExceptionUtils.ensureFound(user);
		SpringExceptionUtils.validateField("changePasswordForm.oldPassword",
			passwordEncoder.matches(changePasswordForm.getOldPassword(),
					oldPassword),
			"com.bpwizard.spring.wrong.password").go();
		
		// sets the password
		user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		userService.save(user, null, null);

		logger.debug("Changed password for user: " + user);
		return user.toUserDto().getUsername();
	}


	public abstract ID toId(String id);

	/**
	 * Updates the fields of the users. Override this if you have more fields.
	 */
	protected void updateUserFields(U user, U updatedUser, UserDto currentUser, Set<String> removedRoles, Set<String> newRoles) {

		logger.debug("Updating user fields for user: " + user);

		// Another good admin must be logged in to edit roles
		if (currentUser.isGoodAdmin() &&
		   !currentUser.getId().equals(user.getId().toString())) { 
			
			logger.debug("Updating roles for user: " + user);

			// update the roles
			
			if (user.getRoles().equals(updatedUser.getRoles())) // roles are same
				return;
			
			if (updatedUser.hasRole(UserUtils.Role.UNVERIFIED)) {
				if (!user.hasRole(UserUtils.Role.UNVERIFIED)) {
					makeUnverified(user, removedRoles, newRoles); // make user unverified
				}
			} else {				
				if (user.hasRole(UserUtils.Role.UNVERIFIED)) {
					user.getRoles().remove(UserUtils.Role.UNVERIFIED); // make user verified
					removedRoles.add(UserUtils.Role.UNVERIFIED);
				}
			}
			
			user.setRoles(updatedUser.getRoles());
			user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		}
	}

	
	/**
	 * Requests for email change.
	 */
	@UserEditPermission
	@Validated(UserUtils.ChangeEmailValidation.class)
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void requestEmailChange(U user, @Valid U updatedUser) {
		
		logger.debug("Requesting email change: " + user);

		// checks
		SpringExceptionUtils.ensureFound(user);	
		SpringExceptionUtils.validateField("updatedUser.password",
			passwordEncoder.matches(updatedUser.getPassword(),
									user.getPassword()),
			"com.bpwizard.spring.wrong.password").go();

		// preserves the new email id
		user.setNewEmail(updatedUser.getNewEmail());
		//user.setChangeEmailCode(BpwUtils.uid());
		userService.save(user, null, null);
		
		// after successful commit, mails a link to the user
		JdbcUtils.afterCommit(() -> mailChangeEmailLink(user));
		
		logger.debug("Requested email change: " + user);		
	}

	
	/**
	 * Mails the change-email verification link to the user.
	 */
	protected void mailChangeEmailLink(U user) {
		
		String changeEmailCode = jweTokenService.createToken(
				JSONWebEncryptionService.CHANGE_EMAIL_AUDIENCE,
				user.getId().toString(), properties.getJwt().getExpirationMillis(),
				SecurityUtils.mapOf("newEmail", user.getNewEmail()));
		
		try {
			
			logger.debug("Mailing change email link to user: " + user);

			// make the link
			String changeEmailLink = properties.getApplicationUrl()
				    + "/users/" + user.getId()
					+ "/change-email?code=" + changeEmailCode;
			
			// mail it
			mailChangeEmailLink(user, changeEmailLink);
			
			logger.debug("Change email link mail queued.");
			
		} catch (Throwable e) {
			// In case of exception, just log the error and keep silent			
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}


	/**
	 * Mails the change-email verification link to the user.
	 * 
	 * Override this method if you're using a different MailData
	 */
	@SuppressWarnings("unchecked")
	protected void mailChangeEmailLink(U user, String changeEmailLink) {
		
		mailSender.send(SpringMailData.of(user.getNewEmail(),
				SpringExceptionUtils.getMessage(
				"com.bpwizard.spring.changeEmailSubject"),
				SpringExceptionUtils.getMessage(
				"com.bpwizard.spring.changeEmailEmail",
				 changeEmailLink)));
	}

	
	/**
	 * Change the email.
	 */
	@PreAuthorize("isAuthenticated()")
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void changeEmail(ID userId, @Valid @NotBlank String changeEmailCode) {
		
		logger.debug("Changing email of current user ...");

		// fetch the current-user
		UserDto currentUser = WebUtils.currentUser();
		
		SpringExceptionUtils.validate(userId.equals(toId(currentUser.getId())),
			"com.bpwizard.spring.wrong.login").go();
		
		U user = userService.findById(userId).orElseThrow(SpringExceptionUtils.notFoundSupplier());
		
		SpringExceptionUtils.validate(StringUtils.hasText(user.getNewEmail()),
				"com.bpwizard.spring.blank.newEmail").go();
		
		JWTClaimsSet claims = jweTokenService.parseToken(changeEmailCode,
				JSONWebEncryptionService.CHANGE_EMAIL_AUDIENCE,
				user.getCredentialsUpdatedMillis());
		
		SecurityUtils.ensureAuthority(
				claims.getSubject().equals(user.getId().toString()) &&
				claims.getClaim("newEmail").equals(user.getNewEmail()),
				"com.bpwizard.spring.wrong.changeEmailCode");
		
		// Ensure that the email would be unique 
		SpringExceptionUtils.validate(
				!userService.findByEmail(user.getNewEmail()).isPresent(),
				"com.bpwizard.spring.duplicate.email").go();	
		
		// update the fields
		user.setEmail(user.getNewEmail());
		user.setNewEmail(null);
		//user.setChangeEmailCode(null);
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		
		// make the user verified if he is not
		Set<String> removedRoles = new HashSet<>();
		if (user.hasRole(UserUtils.Role.UNVERIFIED))
			user.getRoles().remove(UserUtils.Role.UNVERIFIED);
			removedRoles.add(UserUtils.Role.UNVERIFIED);
		
		userService.save(user, removedRoles, null);
		
		// after successful commit,
		JdbcUtils.afterCommit(() -> {
			
			// Login the user
			ServiceUtils.login(user);
		});
		
		logger.debug("Changed email of user: " + user);
	}


//	/**
//	 * Extracts the email id from user attributes received from OAuth2 provider, e.g. Google
//	 * 
//	 */
//	public String getOAuth2Email(String registrationId, Map<String, Object> attributes) {
//
//		return (String) attributes.get(StandardClaimNames.EMAIL);
//	}
//
//	
//	/**
//	 * Extracts additional fields, e.g. name from user attributes received from OAuth2 provider, e.g. Google
//	 * Override this if you introduce more user fields, e.g. name
//	 */
//	public void fillAdditionalFields(String clientId, U user, Map<String, Object> attributes) {
//		
//	}

	
//	/**
//	 * Checks if the account at the OAuth2 provider is verified 
//	 */
//	public boolean getOAuth2AccountVerified(String registrationId, Map<String, Object> attributes) {
//
//		Object verified = attributes.get(StandardClaimNames.EMAIL_VERIFIED);
//		if (verified == null)
//			verified = attributes.get("verified");
//		
//		try {
//			return (boolean) verified;
//		} catch (Throwable t) {
//			return false;
//		}
//	}


	/**
	 * Fetches a new token - for session scrolling etc.
	 * @return 
	 */
	@PreAuthorize("isAuthenticated()")
	public String fetchNewToken(Optional<Long> expirationMillis,
			Optional<String> optionalUsername) {
		
		UserDto currentUser = WebUtils.currentUser();
		String username = optionalUsername.orElse(currentUser.getUsername());
		
		SecurityUtils.ensureAuthority(currentUser.getUsername().equals(username) ||
				currentUser.isGoodAdmin(), "com.bpwizard.spring.notGoodAdminOrSameUser");
		
		return SecurityUtils.TOKEN_PREFIX +
				jwsTokenService.createToken(JSONWebSignatureService.AUTH_AUDIENCE, username,
				expirationMillis.orElse(properties.getJwt().getExpirationMillis()));
	}

	
	/**
	 * Saves the user
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void create(U user) {
		
		userService.create(user);
	}
	
	
	/**
	 * Hides the confidential fields before sending to client
	 */
	protected void hideConfidentialFields(U user) {
		
		user.setPassword(null); // JsonIgnore didn't work
		
		if (!user.hasPermission(WebUtils.currentUser(), UserUtils.Permission.EDIT))
			user.setEmail(null);
		
		logger.debug("Hid confidential fields for user: " + user);
	}

	
	@PreAuthorize("isAuthenticated()")
	public Map<String, String> fetchFullToken(String authHeader) {

		SecurityUtils.ensureCredentials(jwsTokenService.parseClaim(authHeader.substring(SecurityUtils.TOKEN_PREFIX_LENGTH),
				JSONWebSignatureService.USER_CLAIM) == null,	"com.bpwizard.spring.fullTokenNotAllowed");
		
		UserDto currentUser = WebUtils.currentUser();
		
		Map<String, Object> claimMap = Collections.singletonMap(JSONWebSignatureService.USER_CLAIM,
				SecurityUtils.serialize(currentUser)); // Not serializing converts it to a JsonNode
		
		Map<String, String> tokenMap = Collections.singletonMap("token", SecurityUtils.TOKEN_PREFIX +
				jwsTokenService.createToken(JSONWebSignatureService.AUTH_AUDIENCE, currentUser.getUsername(),
					Long.valueOf(properties.getJwt().getShortLivedMillis()),
					claimMap));
		
		return tokenMap;
	}

	
	/**
	 * Adds a BPW-Authorization header to the response
	 */
	public void addAuthHeader(HttpServletResponse response, String username, Long expirationMillis) {
	
		response.addHeader(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME, SecurityUtils.TOKEN_PREFIX +
				jwsTokenService.createToken(JSONWebSignatureService.AUTH_AUDIENCE, username, expirationMillis));
	}
	
	
	public Optional<U> findUserById(String id) {
		return userService.findById(toId(id));
	}
	
	 protected String getShortLivedAuthToken(String username) {
        String shortLivedAuthToken = jwsTokenService.createToken(
				JSONWebSignatureService.AUTH_AUDIENCE,
				username,
				(long) properties.getJwt().getShortLivedMillis());
   
        return shortLivedAuthToken;
    }
}
