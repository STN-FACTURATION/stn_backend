# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .
RUN chmod +x mvnw
RUN ./mvnw package -Pprod -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

# install dos2unix
RUN apt-get update && apt-get install -y dos2unix

# create user
RUN adduser --disabled-password --shell /bin/sh jhipster

# copy files
COPY --from=build --chown=jhipster:jhipster /app/target/*.jar app.jar
COPY --from=build --chown=jhipster:jhipster /app/src/main/docker/jib/entrypoint.sh entrypoint.sh

# fix script
RUN dos2unix entrypoint.sh
RUN chmod +x entrypoint.sh

USER jhipster

ENTRYPOINT ["sh", "-c", "sleep 3600"]

EXPOSE 8081
