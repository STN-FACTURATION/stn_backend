# --------------------

# Stage 1: Build avec Maven Wrapper

# --------------------

FROM eclipse-temurin:21-jdk-ubi9-minimal AS builder

WORKDIR /build

COPY mvnw .

COPY .mvn .mvn

COPY pom.xml .

COPY sonar-project.properties .

RUN chmod +x mvnw

RUN ./mvnw -B -q -e -C dependency:go-offline

COPY src ./src

RUN ./mvnw -B -Pprod -DskipTests clean package

# --------------------

# Stage 2: Runtime

# --------------------

FROM eclipse-temurin:21-jre-ubi9-minimal

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \

    JHIPSTER_SLEEP=0 \

    JAVA_OPTS="" \

    SERVER_PORT=8081

RUN adduser -M -s /bin/sh jhipster

WORKDIR /home/jhipster

COPY --from=builder /build/target/*.jar app.jar

COPY entrypoint.sh entrypoint.sh

RUN chmod 755 entrypoint.sh && chown -R jhipster:jhipster /home/jhipster

USER jhipster

EXPOSE 8081

ENTRYPOINT ["./entrypoint.sh"]
