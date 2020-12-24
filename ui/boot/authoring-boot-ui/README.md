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

# docker push
3. docker push

      About authentication
			There are six different locations searched for credentials. In order, these are:
          o Providing system properties docker.username and docker.password from the outside.
          o Providing system properties registry.username and registry.password from the outside.
          o Using a <authConfig> section in the plugin configuration with <username> and <password> elements.
          o Using OpenShift configuration in ~/.config/kube
          o Using a <server> configuration in ~/.m2/settings.xml
          o Login into a registry with docker login (credentials in a credential helper or in ~/.docker/config.json)

      examples:
      mvn -Ddocker.username=dockerUser -Ddocker.password=pdw docker:push

      docker login container-registry:32000
      mvn  docker:push
