# Static html artifact location: /static/  

Spring Boot will automatically add static web resources located within any of the following directories:

/META-INF/resources/  
/resources/  
/static/  
/public/

# build bpm-ui boot
cd wcm-bpm/angular
mvn clean install -P bpm-ui
cd wcm/bpm/boot/bpm-boot-ui
mvn clean install -DskipTests

mvn spring-boot:run
http://bpm-ui:4009
#

