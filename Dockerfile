FROM openjdk:21-jdk
VOLUME /FileStorage
EXPOSE 8080
COPY target/Moodle-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
