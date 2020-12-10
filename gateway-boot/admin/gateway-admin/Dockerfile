FROM java:openjdk-8-jre-alpine
VOLUME /tmp
ADD target/gateway-admin.jar gateway-admin.jar
EXPOSE 8086
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/gateway-admin.jar"]
