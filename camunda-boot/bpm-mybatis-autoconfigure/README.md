# jasypt
java -cp c:/tools/jasypt/jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=P@ssw0rd password=password  
  // algorithm=PBEWITHMD5ANDDES

Spring Boot OAuth2 Social Login with Google, Facebook, and Github - Part 1
https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/
https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-3/
https://github.com/callicoder/spring-boot-react-oauth2-social-login-demo

#vault
https://computingforgeeks.com/install-and-configure-vault-server-linux/

http://localhost:8080/oauth2/authorize/google?redirect_uri=http://localhost:3000/oauth2/redirect

http://localhost:19090/api-docs
http://localhost:19090/swagger-ui.html

## How to run
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DROOT_LOG_LEVEL=INFO -DAPP_LOG_LEVEL=TRACE -Dlog.root.dir=/var/logs/wcm-bpm-boot"


chmod ugo+w /var/spring-logs/wcm-bpm-boot
 
java -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=DEBUG -Dlog.root.dir=/var/spring-logs/wcm-bpm-boot -jar wcm-bpm-boot-0.0.1-SNAPSHOT.jar 
mvn spring-boot:run -Dspring.profiles.active=dev -Dspring-boot.run.jvmArguments="-Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=DEBUG -Dlog.root.dir=/var/spring-logs/wcm-bpm-boot"

    $ mvn spring-boot:run -Djasypt.encryptor.password=password -Dspring-boot.run.jvmArguments="-DROOT_LOG_LEVEL=INFO -DAPP_LOG_LEVEL=TRACE -Dlog.root.dir=/var/logs/wcm-bpm-boot"

    $ mvn spring-boot:run -Djasypt.encryptor.password=password -Dagentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 -Dspring-boot.run.jvmArguments="-DROOT_LOG_LEVEL=INFO -DAPP_LOG_LEVEL=TRACE -Dlog.root.dir=/var/logs/wcm-bpm-boot"

    java -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=INFO -DAPP_LOG_LEVEL=TRACE -Dlog.root.dir=/var/logs/wcm-bpm-boot -jar target/wcm-bpm-boot-0.0.1-SNAPSHOT.jar
mvn spring-boot:run -Dspring-boot.run.arguments=--mongo

mvn spring-boot:run -Dspring-boot.run.arguments=--mongo -Dspring-boot.run.jvmArguments="-DROOT_LOG_LEVEL=INFO -DAPP_LOG_LEVEL=TRACE -Dlog.root.dir=/var/logs/bpw-wcm"



debug:
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
which exposes [JCR Webdav Server](http://jackrabbit.apache.org/jcr/components/jackrabbit-jcr-server.html#JCR_Webdav_Server) at http://localhost:8080/.


1.
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar  org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="Password@1" password=password algorithm=PBEWithMD5AndDES
mvn -Djasypt.encryptor.password=password spring-boot:run
