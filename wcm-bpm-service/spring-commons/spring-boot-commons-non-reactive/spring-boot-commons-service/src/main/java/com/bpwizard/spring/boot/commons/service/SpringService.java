package com.bpwizard.spring.boot.commons.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.bpwizard.spring.boot.commons.SpringProperties;
import com.bpwizard.spring.boot.commons.domain.ChangePasswordForm;
import com.bpwizard.spring.boot.commons.domain.ResetPasswordForm;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.jpa.JpaUtils;
import com.bpwizard.spring.boot.commons.mail.MailSender;
import com.bpwizard.spring.boot.commons.mail.SpringMailData;
import com.bpwizard.spring.boot.commons.security.BlueTokenService;
import com.bpwizard.spring.boot.commons.security.GreenTokenService;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.security.UserEditPermission;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;
import com.bpwizard.spring.boot.commons.service.util.ServiceUtils;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;
import com.bpwizard.wcm.repo.domain.Role;
import com.bpwizard.wcm.repo.domain.User;
import com.bpwizard.wcm.repo.domain.RoleRepository;
import com.bpwizard.wcm.repo.domain.Tenant;
import com.bpwizard.wcm.repo.domain.TenantRepository;
import com.nimbusds.jwt.JWTClaimsSet;
/**
 * The Spring Commons Service class
 * 
 * @author Sanjay Patel
 */
@Validated
@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public abstract class SpringService
	<U extends AbstractUser<ID>, ID extends Serializable> {

    private static final Logger log = LogManager.getLogger(SpringService.class);
    
	private SpringProperties properties;
	private PasswordEncoder passwordEncoder;
    private MailSender mailSender;
	private AbstractUserRepository<U, ID> userRepository;
	private UserDetailsService userDetailsService;
	private BlueTokenService blueTokenService;
	private GreenTokenService greenTokenService;
	private RoleRepository roleRepository;
	private TenantRepository tenantRepository;
	private Map<String, Role> preloadedRoles;
	
	@Autowired
	public void createSpringService(SpringProperties properties,
			PasswordEncoder passwordEncoder,
			MailSender<?> mailSender,
			AbstractUserRepository<U, ID> userRepository,
			UserDetailsService userDetailsService,
			BlueTokenService blueTokenService,
			GreenTokenService greenTokenService,
			RoleRepository roleRepository,
			TenantRepository tenantRepository,
			Map<String, Role> preloadedRoles) {
		
		this.properties = properties;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
		this.userRepository = userRepository;
		this.userDetailsService = userDetailsService;
		this.blueTokenService = blueTokenService;
		this.greenTokenService = greenTokenService;
		this.roleRepository = roleRepository;
		this.tenantRepository = tenantRepository;
		this.preloadedRoles = preloadedRoles;
		
		log.info("Created");
	}

	
	/**
     * This method is called after the application is ready.
     * Needs to be public - otherwise Spring screams.
     * 
     * @param event
     */
    @EventListener
    public void afterApplicationReady(ApplicationReadyEvent event) {
    	
    	log.info("Starting up Spring Commons ...");
    	onStartup(); // delegate to onStartup()
    	log.info("Spring Commons started");	
    }

    
	/**
	 * Creates the initial Admin user, if not found.
	 * Override this method if needed.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
    public void onStartup() {
    	
		try {
			
			// Check if the user already exists
			userDetailsService
				.loadUserByUsername(properties.getUser()[0].getUsername());
			
		} catch (UsernameNotFoundException e) {
			//TODO: batch mode		
			// Doesn't exist. So, create it.
//			Tenant tenant = new Tenant();
//			tenant.setName("default");
//			tenantRepository.save(tenant);
			Set<Tenant> tenants = null;
			String[] roleNames = properties.getRolename();
			for (String roleName: roleNames) {
				Role role = createRole(roleName, tenants);
				this.preloadedRoles.put(roleName, role);
				
			}
			
			SpringProperties.User[] users = properties.getUser();
			for (SpringProperties.User user: users) {
				createUser(user, this.preloadedRoles, tenants);
			}
		}
	}

	protected Role createRole(String roleName, Set<Tenant> tenants) {
		Role role = new Role();
		role.setName(roleName);
		if (tenants != null) {
			role.setTenants(tenants);
		}
    	roleRepository.save(role);
		return role;
	}

	/**
	 * Creates the initial Admin user.
	 * Override this if needed.
	 */
	protected U createUser(SpringProperties.User user, Map<String, Role> roles, Set<Tenant> tenants) {
    	log.info("Creating the initial user: " + user.getUsername());

    	// create the user
    	U newUser = newUser();
    	newUser.setName(user.getUsername());
    	newUser.setEmail(user.getEmail());
    	newUser.setPassword(passwordEncoder.encode(
				user.getPassword()));
    	if (tenants != null) {
    		newUser.setTenants(tenants);
		}
		for (String rolename: user.getRolename()) {
			if (null != roles.get(rolename)) {
				newUser.getRoles().add(roles.get(rolename));
			}
		}
		userRepository.save(newUser);	
		return newUser;
	}
    
//	/**
//	 * Creates the initial Admin user.
//	 * Override this if needed.
//	 */
//    protected U createAdminUser() {
//		
//    	// fetch data about the user to be created
//    	Admin initialAdmin = properties.getAdmin();
//    	
//    	log.info("Creating the first admin user: " + initialAdmin.getUsername());
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
    public abstract U newUser();

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
		
		log.debug("Getting context ...");

		// make the context
		Map<String, Object> sharedProperties = new HashMap<String, Object>(2);
		sharedProperties.put("reCaptchaSiteKey", properties.getRecaptcha().getSitekey());
		sharedProperties.put("shared", properties.getShared());
		
		UserDto currentUser = WebUtils.currentUser();
		if (currentUser != null)
			addAuthHeader(response, currentUser.getUsername(),
				expirationMillis.orElse(properties.getJwt().getExpirationMillis()));
		
		return SecurityUtils.mapOf(
				"context", sharedProperties,
				"user", WebUtils.currentUser());	
	}
	
	
	/**
	 * Signs up a user.
	 */
	@Validated(UserUtils.SignUpValidation.class)
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void signup(@Valid U user) {
		
		log.debug("Signing up user: " + user);

		initUser(user); // sets right all fields of the user
		userRepository.save(user);
		
		// if successfully committed
		JpaUtils.afterCommit(() -> {
		
			ServiceUtils.login(user); // log the user in
			log.debug("Signed up user: " + user);
		});
	}
	
	
	/**
	 * Initializes the user based on the input data,
	 * e.g. encrypts the password
	 */
	protected void initUser(U user) {
		
		log.debug("Initializing user: " + user);

		user.setPassword(passwordEncoder.encode(user.getPassword())); // encode the password
		makeUnverified(user); // make the user unverified
	}

	
	/**
	 * Makes a user unverified
	 */
	protected void makeUnverified(U user) {
		
		user.getRoles().add(this.preloadedRoles.get(UserUtils.Role.UNVERIFIED));
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		JpaUtils.afterCommit(() -> sendVerificationMail(user)); // send a verification mail to the user
	}
	
	
	/**
	 * Sends verification mail to a unverified user.
	 */
	protected void sendVerificationMail(final U user) {
		try {
			
			log.debug("Sending verification mail to: " + user);
			
			String verificationCode = greenTokenService.createToken(
					GreenTokenService.VERIFY_AUDIENCE,
					user.getId().toString(), properties.getJwt().getExpirationMillis(),
					SecurityUtils.mapOf("email", user.getEmail()));

			// make the link
			String verifyLink = properties.getApplicationUrl()
				+ "/users/" + user.getId() + "/verification?code=" + verificationCode;

			// send the mail
			sendVerificationMail(user, verifyLink);

			log.debug("Verification mail to " + user.getEmail() + " queued.");
			
		} catch (Throwable e) {
			// In case of exception, just log the error and keep silent
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}	

	
	/**
	 * Sends verification mail to a unverified user.
	 * Override this method if you're using a different MailData
	 */
	protected void sendVerificationMail(final U user, String verifyLink) {
		
		// send the mail
		mailSender.send(SpringMailData.of(user.getEmail(),
			SpringExceptionUtils.getMessage("com.bpwizard.spring.verifySubject"),
			SpringExceptionUtils.getMessage(
				"com.bpwizard.spring.verifyEmail",	verifyLink)));
	}	

	
	/**
	 * Resends verification mail to the user.
	 */
	@UserEditPermission
	public void resendVerificationMail(U user) {

		// The user must exist
		SpringExceptionUtils.ensureFound(user);
		
		// must be unverified
		SpringExceptionUtils.validate(user.getRoles().contains(UserUtils.Role.UNVERIFIED),
				"com.bpwizard.spring.alreadyVerified").go();	

		// send the verification mail
		sendVerificationMail(user);
	}

	
	/**
	 * Fetches a user by email
	 */
	public U fetchUserByEmail(@Valid @Email @NotBlank String email) {
		
		log.debug("Fetching user by email: " + email);
		return processUser(userRepository.findByEmail(email).orElse(null));
	}

	
	/**
	 * Returns a non-null, processed user for the client.
	 */
	public U processUser(U user) {
		
		log.debug("Fetching user: " + user);

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
		
		log.debug("Verifying user ...");

		U user = userRepository.findById(userId).orElseThrow(SpringExceptionUtils.notFoundSupplier());
		
		// ensure that he is unverified
		SpringExceptionUtils.validate(user.hasRole(UserUtils.Role.UNVERIFIED),
				"com.bpwizard.spring.alreadyVerified").go();	
		
		JWTClaimsSet claims = greenTokenService.parseToken(verificationCode,
				GreenTokenService.VERIFY_AUDIENCE, user.getCredentialsUpdatedMillis());
		
		SecurityUtils.ensureAuthority(
				claims.getSubject().equals(user.getId().toString()) &&
				claims.getClaim("email").equals(user.getEmail()),
				"com.bpwizard.spring.wrong.verificationCode");
		
		user.getRoles().remove(UserUtils.Role.UNVERIFIED); // make him verified
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		userRepository.save(user);
		
		// after successful commit,
		JpaUtils.afterCommit(() -> {
			
			// Re-login the user, so that the UNVERIFIED role is removed
			ServiceUtils.login(user);
			log.debug("Re-logged-in the user for removing UNVERIFIED role.");		
		});
		
		log.debug("Verified user: " + user);		
	}

	
	/**
	 * Forgot password.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void forgotPassword(@Valid @Email @NotBlank String email) {
		
		log.debug("Processing forgot password for email: " + email);
		
		// fetch the user record from database
		U user = userRepository.findByEmail(email)
				.orElseThrow(SpringExceptionUtils.notFoundSupplier());

		mailForgotPasswordLink(user);
	}
	
	
	/**
	 * Mails the forgot password link.
	 * 
	 * @param user
	 */
	public void mailForgotPasswordLink(U user) {
		
		log.debug("Mailing forgot password link to user: " + user);

		String forgotPasswordCode = greenTokenService.createToken(
				GreenTokenService.FORGOT_PASSWORD_AUDIENCE,
				user.getEmail(), properties.getJwt().getExpirationMillis());

		// make the link
		String forgotPasswordLink =	properties.getApplicationUrl()
			    + "/reset-password?code=" + forgotPasswordCode;
		
		mailForgotPasswordLink(user, forgotPasswordLink);
		
		log.debug("Forgot password link mail queued.");
	}

	
	/**
	 * Mails the forgot password link.
	 * 
	 * Override this method if you're using a different MailData
	 */
	public void mailForgotPasswordLink(U user, String forgotPasswordLink) {
		
		// send the mail
		mailSender.send(SpringMailData.of(user.getEmail(),
				SpringExceptionUtils.getMessage("com.bpwizard.spring.forgotPasswordSubject"),
				SpringExceptionUtils.getMessage("com.bpwizard.spring.forgotPasswordEmail",
					forgotPasswordLink)));
	}
	
	/**
	 * Resets the password.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void resetPassword(@Valid ResetPasswordForm form) {
		
		log.debug("Resetting password ...");

		JWTClaimsSet claims = greenTokenService.parseToken(form.getCode(),
				GreenTokenService.FORGOT_PASSWORD_AUDIENCE);
		
		String email = claims.getSubject();
		
		// fetch the user
		U user = userRepository.findByEmail(email).orElseThrow(SpringExceptionUtils.notFoundSupplier());
		ServiceUtils.ensureCredentialsUpToDate(claims, user);
		
		// sets the password
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		//user.setForgotPasswordCode(null);
		
		userRepository.save(user);
		
		// after successful commit,
		JpaUtils.afterCommit(() -> {
			
			// Login the user
			ServiceUtils.login(user);
		});
		
		log.debug("Password reset.");		
	}

	
	/**
	 * Updates a user with the given data.
	 */
	@UserEditPermission
	@Validated(UserUtils.UpdateValidation.class)
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public UserDto updateUser(U user, @Valid U updatedUser) {
		
		log.debug("Updating user: " + user);

		// checks
		JpaUtils.ensureCorrectVersion(user, updatedUser);

		// delegates to updateUserFields
		updateUserFields(user, updatedUser, WebUtils.currentUser());
		userRepository.save(user);
		
		log.debug("Updated user: " + user);
		
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
		
		log.debug("Changing password for user: " + user);
		
		// Get the old password of the logged in user (logged in user may be an ADMIN)
		UserDto currentUser = WebUtils.currentUser();
		U loggedIn = userRepository.findById(toId(currentUser.getId())).get();
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
		userRepository.save(user);

		log.debug("Changed password for user: " + user);
		return user.toUserDto().getUsername();
	}


	public abstract ID toId(String id);

	/**
	 * Updates the fields of the users. Override this if you have more fields.
	 */
	protected void updateUserFields(U user, U updatedUser, UserDto currentUser) {

		log.debug("Updating user fields for user: " + user);

		// Another good admin must be logged in to edit roles
		if (currentUser.isGoodAdmin() &&
		   !currentUser.getId().equals(user.getId().toString())) { 
			
			log.debug("Updating roles for user: " + user);

			// update the roles
			
			if (user.getRoles().equals(updatedUser.getRoles())) // roles are same
				return;
			
			if (updatedUser.hasRole(UserUtils.Role.UNVERIFIED)) {
				
				if (!user.hasRole(UserUtils.Role.UNVERIFIED)) {

					makeUnverified(user); // make user unverified
				}
			} else {
				
				if (user.hasRole(UserUtils.Role.UNVERIFIED))
					user.getRoles().remove(UserUtils.Role.UNVERIFIED); // make user verified
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
		
		log.debug("Requesting email change: " + user);

		// checks
		SpringExceptionUtils.ensureFound(user);	
		SpringExceptionUtils.validateField("updatedUser.password",
			passwordEncoder.matches(updatedUser.getPassword(),
									user.getPassword()),
			"com.bpwizard.spring.wrong.password").go();

		// preserves the new email id
		user.setNewEmail(updatedUser.getNewEmail());
		//user.setChangeEmailCode(BpwUtils.uid());
		userRepository.save(user);
		
		// after successful commit, mails a link to the user
		JpaUtils.afterCommit(() -> mailChangeEmailLink(user));
		
		log.debug("Requested email change: " + user);		
	}

	
	/**
	 * Mails the change-email verification link to the user.
	 */
	protected void mailChangeEmailLink(U user) {
		
		String changeEmailCode = greenTokenService.createToken(
				GreenTokenService.CHANGE_EMAIL_AUDIENCE,
				user.getId().toString(), properties.getJwt().getExpirationMillis(),
				SecurityUtils.mapOf("newEmail", user.getNewEmail()));
		
		try {
			
			log.debug("Mailing change email link to user: " + user);

			// make the link
			String changeEmailLink = properties.getApplicationUrl()
				    + "/users/" + user.getId()
					+ "/change-email?code=" + changeEmailCode;
			
			// mail it
			mailChangeEmailLink(user, changeEmailLink);
			
			log.debug("Change email link mail queued.");
			
		} catch (Throwable e) {
			// In case of exception, just log the error and keep silent			
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}


	/**
	 * Mails the change-email verification link to the user.
	 * 
	 * Override this method if you're using a different MailData
	 */
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
		
		log.debug("Changing email of current user ...");

		// fetch the current-user
		UserDto currentUser = WebUtils.currentUser();
		
		SpringExceptionUtils.validate(userId.equals(toId(currentUser.getId())),
			"com.bpwizard.spring.wrong.login").go();
		
		U user = userRepository.findById(userId).orElseThrow(SpringExceptionUtils.notFoundSupplier());
		
		SpringExceptionUtils.validate(StringUtils.isNotBlank(user.getNewEmail()),
				"com.bpwizard.spring.blank.newEmail").go();
		
		JWTClaimsSet claims = greenTokenService.parseToken(changeEmailCode,
				GreenTokenService.CHANGE_EMAIL_AUDIENCE,
				user.getCredentialsUpdatedMillis());
		
		SecurityUtils.ensureAuthority(
				claims.getSubject().equals(user.getId().toString()) &&
				claims.getClaim("newEmail").equals(user.getNewEmail()),
				"com.bpwizard.spring.wrong.changeEmailCode");
		
		// Ensure that the email would be unique 
		SpringExceptionUtils.validate(
				!userRepository.findByEmail(user.getNewEmail()).isPresent(),
				"com.bpwizard.spring.duplicate.email").go();	
		
		// update the fields
		user.setEmail(user.getNewEmail());
		user.setNewEmail(null);
		//user.setChangeEmailCode(null);
		user.setCredentialsUpdatedMillis(System.currentTimeMillis());
		
		// make the user verified if he is not
		if (user.hasRole(UserUtils.Role.UNVERIFIED))
			user.getRoles().remove(UserUtils.Role.UNVERIFIED);
		
		userRepository.save(user);
		
		// after successful commit,
		JpaUtils.afterCommit(() -> {
			
			// Login the user
			ServiceUtils.login(user);
		});
		
		log.debug("Changed email of user: " + user);
	}


	/**
	 * Extracts the email id from user attributes received from OAuth2 provider, e.g. Google
	 * 
	 */
	public String getOAuth2Email(String registrationId, Map<String, Object> attributes) {

		return (String) attributes.get(StandardClaimNames.EMAIL);
	}

	
	/**
	 * Extracts additional fields, e.g. name from user attributes received from OAuth2 provider, e.g. Google
	 * Override this if you introduce more user fields, e.g. name
	 */
	public void fillAdditionalFields(String clientId, U user, Map<String, Object> attributes) {
		
	}

	
	/**
	 * Checks if the account at the OAuth2 provider is verified 
	 */
	public boolean getOAuth2AccountVerified(String registrationId, Map<String, Object> attributes) {

		Object verified = attributes.get(StandardClaimNames.EMAIL_VERIFIED);
		if (verified == null)
			verified = attributes.get("verified");
		
		try {
			return (boolean) verified;
		} catch (Throwable t) {
			return false;
		}
	}


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
				blueTokenService.createToken(BlueTokenService.AUTH_AUDIENCE, username,
				expirationMillis.orElse(properties.getJwt().getExpirationMillis()));
	}

	
	/**
	 * Saves the user
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void save(U user) {
		
		userRepository.save(user);
	}
	
	
	/**
	 * Hides the confidential fields before sending to client
	 */
	protected void hideConfidentialFields(U user) {
		
		user.setPassword(null); // JsonIgnore didn't work
		
		if (!user.hasPermission(WebUtils.currentUser(), UserUtils.Permission.EDIT))
			user.setEmail(null);
		
		log.debug("Hid confidential fields for user: " + user);
	}

	
	@PreAuthorize("isAuthenticated()")
	public Map<String, String> fetchFullToken(String authHeader) {

		SecurityUtils.ensureCredentials(blueTokenService.parseClaim(authHeader.substring(SecurityUtils.TOKEN_PREFIX_LENGTH),
				BlueTokenService.USER_CLAIM) == null,	"com.bpwizard.spring.fullTokenNotAllowed");
		
		UserDto currentUser = WebUtils.currentUser();
		
		Map<String, Object> claimMap = Collections.singletonMap(BlueTokenService.USER_CLAIM,
				SecurityUtils.serialize(currentUser)); // Not serializing converts it to a JsonNode
		
		Map<String, String> tokenMap = Collections.singletonMap("token", SecurityUtils.TOKEN_PREFIX +
				blueTokenService.createToken(BlueTokenService.AUTH_AUDIENCE, currentUser.getUsername(),
					Long.valueOf(properties.getJwt().getShortLivedMillis()),
					claimMap));
		
		return tokenMap;
	}

	
	/**
	 * Adds a BPW-Authorization header to the response
	 */
	public void addAuthHeader(HttpServletResponse response, String username, Long expirationMillis) {
	
		response.addHeader(SecurityUtils.TOKEN_RESPONSE_HEADER_NAME, SecurityUtils.TOKEN_PREFIX +
				blueTokenService.createToken(BlueTokenService.AUTH_AUDIENCE, username, expirationMillis));
	}
	
	
	public Optional<U> findUserById(String id) {
		return userRepository.findById(toId(id));
	}
}
