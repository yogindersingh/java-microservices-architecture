FROM amazoncorretto:21-alpine-jdk

LABEL "org.opencontainers.image.authors"="yogindersingh634"

EXPOSE 8080

COPY target/Accounts.jar .

ENTRYPOINT ["java", "-jar", "Accounts.jar"]