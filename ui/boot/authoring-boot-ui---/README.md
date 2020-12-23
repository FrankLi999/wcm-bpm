# Static html artifact location: /static/  

Spring Boot will automatically add static web resources located within any of the following directories:

/META-INF/resources/  
/resources/  
/static/  
/public/

# build bpm-ui boot
cd wcm-bpm/angular
mvn clean install -P wcm-authoring
cd wcm/bpm/boot/authoring-boot-ui
mvn clean install -DskipTests
mvn spring-boot:run
http://wcm-authoring:3009
#


