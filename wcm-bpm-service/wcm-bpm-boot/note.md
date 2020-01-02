Key-cloak

install:

https://www.thomasvitale.com/introducing-keycloak-identity-access-management/
   bin/standalone.sh -Djboss.socket.binding.port-offset=100
   bin\standalone.bat -Djboss.socket.binding.port-offset=100
   
   http://localhost:8180
   admin/admin, After logging in, we are inside the Master realm

   What is a realm? It's a domain in which we apply specific security policies. The Master realm is the parent 
   of any realm we could   create. We want to create a new realm, which will be a new security domain specifically 
   for our web applications or services.
   
   
basic configuration:   
https://www.thomasvitale.com/keycloak-configuration-authentication-authorisation/
create a new realm: wcm_bpm
    select "Add realm" button,
Define Roles for Users
     a Member role with normal privileges and a Librarian role with administrative privileges.
Add Users/credential/assign roles
User Account Service   
    http://localhost:8180/auth/realms/public-library/account/
    
Keycloak Authentication Flows, SSO Protocols and Client Configuration
    https://www.thomasvitale.com/keycloak-authentication-flow-sso-client/
        OpenID Connect (on top of OAuth 2.0) and SAML     
     
    authentication flow:
       
    Keycloak provides already several authentication flows that you can customise in 
    Authentication > Flows. Should you need something different, you can always create 
    your own by choosing New in the far right of the screen.
    
    SSO Protocols:  OpenID Connect and SAML.
       OIDC makes use of JWT (JSON Web Token) in the form of identity tokens and access tokens.
    
       Formalising the concepts standardised by the OAuth 2.0 framework, OIDC defines four 
       main flows that can be used to authenticate a user:

       > Authorization Code Flow for browser-based applications like SPAs (Single Page Applications) or 
         server-side application;
       > Implicit Flow for browser-based application, less secure than the previous one, not recommended;
       > Client Credentials Grant for REST clients like web services, it involves storing a secret, 
         so the client is supposed to be trustworthy;
       > Resource Owner Password Credentials Grant for REST clients like interfaces to mainframes and 
         other legacy systems which cannot support modern authentication protocols, it involves sharing 
         credentials with another service, caution here.    
         
      Client Configuration: Clients are entities that can request authentication of a user.
         Client ID: wcm_bpm
         Client protocol: OIDC
         Root URL: http://localhost:8080
         
         An Identity Token, which contains information about the logged user such as the username 
         and the email.
		  An Access Token, digitally signed by the realm, which contains access data such as the 
			roles assigned to the logged user.
    https://www.thomasvitale.com/spring-boot-keycloak-security/
    https://www.thomasvitale.com/spring-security-keycloak/
/////////////////////////////////////////////////////////////////////////////
Spring boot/security with camunda

https://github.com/camunda-consulting/code/tree/master/snippets/springboot-security-sso

https://github.com/camunda-consulting/code/tree/master/snippets/springboot-keycloak-sso
https://github.com/VonDerBeck/camunda-identity-keycloak

https://github.com/camunda/camunda-sso-jboss
As a particular example, this project shows how to do SSO with Kerberos/Active Directory and Wildfly
////////////////////////////////////////////////////////////////////////////////////////////
1. https://camunda.com/bpmn/examples/https://camunda.com/bpmn/examples/https://camunda.com/bpmn/examples/
2. https://github.com/camunda-consulting/code/tree/master/snippets
3. https://camunda.com/best-practices/routing-events-to-processes/