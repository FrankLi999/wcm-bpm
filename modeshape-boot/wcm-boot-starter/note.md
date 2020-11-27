## FAKE Mail server

http://nilhcem.com/FakeSMTP/

> cd C:\tools\FakeSMTP-2.0\target

> with GUI
java -jar fakeSMTP-2.0.jar -s -p 2525 -a 127.0.0.1 -m

> NO Gui:
java -jar fakeSMTP-2.0.jar -s -b -p 2525 -a 127.0.0.1


> No need to save email to file system: -m
## Glowroot path

https://glowroot.org/

~/tools/glowroot-0.13.5/glowroot.jar

add -javaagent:~/tools/glowroot-0.13.5/glowroot.jar to your application's JVM args

http://localhost:4000


linux:
-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=DEBUG -Dlog.root.dir=/var/spring-logs/wcm-bpm-boot -javaagent:/home/frank/tools/glowroot-0.13.5/glowroot.jar

windows:
-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=DEBUG -Dlog.root.dir=c:/var/spring-logs/wcm-bpm-boot -javaagent:c:/tools/glowroot-0.13.5/glowroot.jar


## Local Docker

 docker run --detach --env MYSQL_ROOT_PASSWORD=P@ssw0rd --env MYSQL_USER=wcmbpm --env MYSQL_PASSWORD=P@ssw0rd --env MYSQL_DATABASE=wcm_bpm --name mysql --publish 3306:3306 mysql:8.0.16

 apiman local:
 
 docker pull apiman/on-wildfly11 size 1.18G
 docker run -it -p 8080:8080 -p 8443:8443 --name apiman apiman/on-wildfly11:latest
 
 http://192.168.0.168:8080/apimanui
    admin/admin123!
 	
 gravitee local:
 graviteeio/gateway
 graviteeio/management-api
 graviteeio/management-ui
 docker run --publish 82:8082  --name gateway  --detach  graviteeio/gateway:latest
 docker run --publish 81:8083 --name management-api --detach graviteeio/management-api:latest
 docker run --publish 80:80 --env MGMT_API_URL=http://localhost:81/management/ --name management-ui --detach graviteeio/management-ui:latest
 
 docker compose with API Management + MongoDB + Elasticsearch
 
 
 graviteeio/gateway: http://localhost:8000
 graviteeio/management-api: http://localhost:8005/management/
 graviteeio/management-ui: http://localhost:8002

  admin /adminadmin
  
 # Download compose files
 $ curl -L https://raw.githubusercontent.com/gravitee-io/gravitee-docker/master/environments/demo/common.yml -o "common.yml"
 $ curl -L https://raw.githubusercontent.com/gravitee-io/gravitee-docker/master/environments/demo/docker-compose-local.yml -o "docker-compose-local.yml"

 # (Optional step: pull to ensure that you are running latest images)
 $ docker-compose -f docker-compose-local.yml pull

 # And run...
 $ docker-compose -f docker-compose-local.yml up

 
## Openshift CodeReady
crc start -n 8.8.8.8

https://manage.openshift.com/account/index

oc import-image openjdk18 --from=registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift --confirm
oc create -f openshift4/credentials-secret.yml
oc create rolebinding default-view --clusterrole=view --serviceaccount=wcm-bpm:default --namespace=wcm-bpm

mvn clean install -Popenshift -DskipTests
mvn fabric8:resource -Popenshift  -DskipTests

oc create -f openshift4/wcm-bpm-boot-deploymentconfig.yml
oc create -f openshift4/wcm-bpm-boot-service.yml
oc create -f openshift4/wcm-bpm-boot-route.yml
-- mvn fabric8:build -Popenshift  -DskipTest
-- mvn clean fabric8:deploy -P openshift  -DskipTests
oc get pods -w


## MySQL Openshift
 https://github.com/sclorg/mysql-container/blob/master/8.0/root/usr/share/container-scripts/mysql/README.md
 https://docs.okd.io/latest/using_images/db_images/mysql.html
 
oc port-forward mysql-1-p89pg 3306:3306

oc new-app mysql:8.0~https://github.com/sclorg/mysql-container.git --name mysql --env MYSQL_SERVICE_HOST=mysql --env MYSQL_SERVICE_PORT=3306 --env MYSQL_DATABASE=wcm_bpm --env MYSQL_USER=wcmbpm --env MYSQL_PASSWORD=P@ssw0rd --env MYSQL_OPERATIONS_USER=mysql --env MYSQL_OPERATIONS_PASSWORD=P@ssw0rd --env MYSQL_ROOT_PASSWORD=P@ssw0rd --env MEMORY_LIMIT=1024mi

oc expose svc/mysql

GRANT ALL ON *.* TO 'wcmbpm'@'%';
flush privileges;   
## Key-cloak

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
         Client ID: public_library
         Client protocol: OIDC
         Root URL: http://localhost:8080
         
         
         Client ID: wcm_bpm
         Client protocol: OIDC
         Root URL: http://localhost:8080
         
          Access Type         : confidential
			Valid Redirect URIs : http://localhost:8080
			# Required for micro-service to micro-service secured calls
			Service Accounts Enabled : On
			Authorization Enabled : On
         
         Note: Access Type confidential supports getting access token using client credentials grant 
         as well as authorization code grant. If a micro-service need to call another micro-service, 
         caller will be ‘confidential’ and callee will be ‘bearer-only’.
         
         An Identity Token, which contains information about the logged user such as the username 
         and the email.
		  An Access Token, digitally signed by the realm, which contains access data such as the 
			roles assigned to the logged user.
			
			
			Create a Mapper (To get user_name in access token)
			Get Configuration From OpenID Configuration Endpoint
				 http://localhost:8180/auth/realms/wcm_bpm/.well-known/openid-configuration
				 
				 Important URLS to be copied from response:
					issuer : http://localhost:8180/auth/realms/wcm_bpm
					authorization_endpoint: ${issuer}/protocol/openid-connect/auth
					token_endpoint: ${issuer}/protocol/openid-connect/token
					token_introspection_endpoint: ${issuer}/protocol/openid-connect/token/introspect
					userinfo_endpoint: ${issuer}/protocol/openid-connect/userinfo
					Response also contains grant types and scopes supported
					grant_types_supported: ["client_credentials", …]
					scopes_supported: ["openid", …]
					
					
					
					authorization_endpoint: "http://localhost:8180/auth/realms/wcm_bpm/protocol/openid-connect/auth",
					token_endpoint: "http://localhost:8180/auth/realms/wcm_bpm/protocol/openid-connect/token",
					token_introspection_endpoint: "http://localhost:8180/auth/realms/wcm_bpm/protocol/openid-connect/token/introspect",
					userinfo_endpoint: "http://localhost:8180/auth/realms/wcm_bpm/protocol/openid-connect/userinfo",
					end_session_endpoint: "http://localhost:8180/auth/realms/wcm_bpm/protocol/openid-connect/logout",
					jwks_uri: "http://localhost:8180/auth/realms/wcm_bpm/protocol/openid-connect/certs",
					check_session_iframe: "http://localhost:8180/auth/realms/wcm_bpm/protocol/openid-connect/login-status-iframe.html",
					
					grant_types_supported: [
						"authorization_code",
						"implicit",
						"refresh_token",
						"password",
						"client_credentials"
					]
					scopes_supported: [
						"openid",
						"address",
						"email",
						"microprofile-jwt",
						"offline_access",
						"phone",
						"profile",
						"roles",
						"web-origins"
					],
					
		Use postman to get the token and decode it on https://jwt.io/
    https://www.thomasvitale.com/spring-boot-keycloak-security/
    https://www.thomasvitale.com/spring-security-keycloak/
    https://ordina-jworks.github.io/security/2019/08/22/Securing-Web-Applications-With-Keycloak.html
    https://medium.com/@bcarunmail/securing-rest-api-using-keycloak-and-spring-oauth2-6ddf3a1efcc2
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