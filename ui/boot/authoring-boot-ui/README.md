# Static html artifact location: /static/  

Spring Boot will automatically add static web resources located within any of the following directories:

/META-INF/resources/  
/resources/  
/static/  
/public/

# build bpm-ui boot
cd wcm-bpm/angular
mvn clean install -Dangular-project=wcm=authoring
cd wcm/bpm/boot/bpm-boot-ui
mvn clean install -DskipTests

mvn spring-boot:run
http://bpm-ui:4009

# sonar/jcoco

mvn clean verify sonar:sonar -P sonar,coverage
# create docker image
  
  pom properties:
    <container-port>3009:3009</container-port>
    <probe-url>http://localhost:3009/actuator/health</probe-url>

  >mvn package docker:build -P docker

