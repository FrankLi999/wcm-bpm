#FROM openjdk:11
FROM openjdk:8-jdk-alpine
VOLUME /var/logs
VOLUME /var/data
EXPOSE 3009
#ADD target/*.jar app.jar

ARG JAR_FILE
COPY ${JAR_FILE} /var/data/app/app.jar
#ADD h2/wcm_bpm_db.mv.db  /var/data/app/h2/wcm_bpm_db.mv.db
WORKDIR /var/data/app
RUN sh -c 'touch app.jar'
ENV JAVA_OPTS="-DLOG_PATH=/var/logs"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]