Spring Boot OAuth2 Social Login with Google, Facebook, and Github - Part 1
https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/
https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-3/
https://github.com/callicoder/spring-boot-react-oauth2-social-login-demo



http://localhost:8080/oauth2/authorize/google?redirect_uri=http://localhost:3000/oauth2/redirect

http://localhost:19090/api-docs
http://localhost:19090/swagger-ui.html

## How to run

    $ mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DLOG_ROOT_LEVEL=INFO -DAPP_LOG_LEVEL=TRACE -Dlog.root.dir=/var/logs/modeshape-repo"

mvn spring-boot:run -Dspring-boot.run.arguments=--mongo

mvn spring-boot:run -Dspring-boot.run.arguments=--mongo -Dspring-boot.run.jvmArguments="-DLOG_ROOT_LEVEL=INFO -DAPP_LOG_LEVEL=TRACE -Dlog.root.dir=/var/logs/bpw-repo-reactive"



debug:
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
which exposes [JCR Webdav Server](http://jackrabbit.apache.org/jcr/components/jackrabbit-jcr-server.html#JCR_Webdav_Server) at http://localhost:8080/.


1.
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar  org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="Password@1" password=password algorithm=PBEWithMD5AndDES
mvn -Djasypt.encryptor.password=password spring-boot:run
