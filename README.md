# Java-microservices-architecture


# Commands

Run service using maven : mvn spring-boot:run

Build docker image using docker file : docker build . -t {{registry}}/{{imageName}}:{{version}}

Build docker image using maven (Build packs): mvn spring-boot:build-image

Build docker image using maven (Google jib): mvn compile jib:dockerBuild