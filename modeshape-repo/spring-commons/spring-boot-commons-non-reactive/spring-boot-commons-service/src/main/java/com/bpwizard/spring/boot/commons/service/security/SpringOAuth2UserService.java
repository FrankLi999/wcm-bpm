package com.bpwizard.spring.boot.commons.service.security;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;
import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.service.SpringService;
import com.bpwizard.spring.boot.commons.service.domain.AbstractUser;
import com.bpwizard.spring.boot.commons.util.SecurityUtils;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.bpwizard.wcm.repo.exception.OAuth2AuthenticationProcessingException;
import com.bpwizard.wcm.repo.secureity.oauth2.AuthProvider;
import com.bpwizard.wcm.repo.secureity.oauth2.user.OAuth2UserInfo;
import com.bpwizard.wcm.repo.secureity.oauth2.user.OAuth2UserInfoFactory;

/**
 * Logs in or registers a user after OAuth2 SignIn/Up
 */
public class SpringOAuth2UserService<U extends AbstractUser<ID>, ID extends Serializable> extends DefaultOAuth2UserService {

	private static final Logger log = LogManager.getLogger(SpringOAuth2UserService.class);

	private SpringUserDetailsService<U, ?> userDetailsService;
	private SpringService<U, ?> springService;
	private PasswordEncoder passwordEncoder;

	public SpringOAuth2UserService(
			SpringUserDetailsService<U, ?> userDetailsService,
			SpringService<U, ?> springService,
			PasswordEncoder passwordEncoder) {

		this.userDetailsService = userDetailsService;
		this.springService = springService;
		this.passwordEncoder = passwordEncoder;
		
		replaceRestOperarions();
		log.info("Created");
	}

	protected void replaceRestOperarions() {
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		restTemplate.setMessageConverters(makeMessageConverters());
		setRestOperations(restTemplate);
		
		log.info("Rest Operations replaced");
	}

	protected List<HttpMessageConverter<?>> makeMessageConverters() {
		
		log.info("Making message converters");

		MappingJackson2HttpMessageConverter converter =	new MappingJackson2HttpMessageConverter();

        List<MediaType> mediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        mediaTypes.add(MediaType.asMediaType(new MimeType("text", "javascript", StandardCharsets.UTF_8))); // Facebook returns text/javascript        

        converter.setSupportedMediaTypes(mediaTypes);
        return Collections.singletonList(converter);
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oath2User = super.loadUser(userRequest);
		return buildPrincipal(userRequest, oath2User);
	}

	/**
	 * Builds the security principal from the given userReqest.
	 * Registers the user if not already reqistered
	 */
	public SpringPrincipal buildPrincipal(OAuth2UserRequest userRequest, OAuth2User oath2User) {
		String registrationId = userRequest.getClientRegistration().getRegistrationId();	
		
		Map<String, Object> attributes = oath2User.getAttributes();
		OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
				registrationId, 
				attributes);
		String email = springService.getOAuth2Email(registrationId, attributes);
    	Optional<U> optionalUser = userDetailsService.findUserByEmail(email);
    	U user;
    	if (optionalUser.isPresent()) {
    		user = optionalUser.get();
//    		if(!user.getProvider().equals(AuthProvider.valueOf(registrationId))) {
//                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
//                        user.getProvider() + " account. Please use your " + user.getProvider() +
//                        " account to login.");
//            }
    		// user = updateExistingUser(user, oauth2UserInfo);
    	} else {
    		user = this.registerNewUser(registrationId, email, attributes, oauth2UserInfo);
			try {
				springService.mailForgotPasswordLink(user);
			} catch (Throwable e) {
				// In case of exception, just log the error and keep silent			
				log.error(ExceptionUtils.getStackTrace(e));
			}
		}
    	UserDto userDto = user.toUserDto();
    	SpringPrincipal principal = new SpringPrincipal(userDto);
		principal.setAttributes(attributes);
		principal.setName(oauth2UserInfo.getName());
		return principal;
	} 
	
	private U registerNewUser(String registrationId, String email, Map<String, Object> attributes, OAuth2UserInfo oauth2UserInfo) {
		
		SpringExceptionUtils.validate(StringUtils.hasText(email), "com.bpwizard.spring.oauth2EmailNeeded", registrationId).go();
		
		boolean emailVerified = springService.getOAuth2AccountVerified(registrationId, attributes);
		// SpringExceptionUtils.validate(emailVerified, "com.bpwizard.spring.oauth2EmailNotVerified", registrationId).go();
		
		U user = springService.newUser();
		user.setName(oauth2UserInfo.getName());
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(SecurityUtils.uid()));
		user.setEmailVerified(emailVerified);
		user.setImageUrl(oauth2UserInfo.getImageUrl());
		user.setProvider(AuthProvider.valueOf(registrationId));
		user.setProviderId(oauth2UserInfo.getId());
		user.getRoles().add(UserUtils.Role.USER);
		springService.fillAdditionalFields(registrationId, user, attributes);
		springService.save(user);
		return user;
    }
	
	private U updateExistingUser(U existingUser, OAuth2UserInfo oauth2UserInfo) {
        existingUser.setName(oauth2UserInfo.getName());
        existingUser.setImageUrl(oauth2UserInfo.getImageUrl());
        springService.save(existingUser);
        return existingUser;
    }
}
