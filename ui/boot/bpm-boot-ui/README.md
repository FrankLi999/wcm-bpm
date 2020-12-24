# Static html artifact location: /static/  

Spring Boot will automatically add static web resources located within any of the following directories:

/META-INF/resources/  
/resources/  
/static/  
/public/

# build bpm-ui boot
cd wcm-bpm/angular
  mvn clean install -Dangular-project=bpm-ui
cd wcm/bpm/boot/bpm-boot-ui
mvn clean install -DskipTests

mvn spring-boot:run
http://bpm-ui:4009

# sonar/jcoco

mvn clean verify sonar:sonar -P sonar,coverage
# create docker image
  
  pom properties:
    <container-port>4009:4009</container-port>
    <probe-url>http://localhost:4009/actuator/health</probe-url>
    <project.artifact.name>${project.artifactId}</project.artifact.name>
    <build.number>LOCAL</build.number>
    build final name:
    <finalName>${project.artifact.name}-${project.version}-${build.number}</finalName>
  > Create Dockerfile
    
  >mvn package docker:build -P docker


#  docker run and clean up
   ---------------------------------
   with maven exec plugin:
      mvn exec:exec -P docker-run
      mvn exec:exec -P docker-stop-container
      mvn exec:exec -P docker-remove-container
      mvn exec:exec -P docker-remove-image
   ------------------------------------
   with fabric 8, maven docker plugin:
      docker:start or docker:run
   
   -------------------------------------

# docker push
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

      > docker login container-registry:32000
      > mvn  docker:push   -P docker