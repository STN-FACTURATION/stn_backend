# --------------------
# Stage 1: Build
# --------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy only pom first (cache deps)
COPY pom.xml .
RUN mvn -B -q -e -C dependency:go-offline

# Copy source
COPY src ./src

# Build jar
RUN mvn -B -DskipTests clean package

# --------------------
# Stage 2: Runtime
# --------------------
FROM eclipse-temurin:21-jre-ubi9-minimal

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS="" \
    SERVER_PORT=8080

RUN adduser -M -s /bin/sh jhipster

WORKDIR /home/jhipster

# Copy jar from builder
COPY --from=builder /build/target/*.jar app.jar

# Copy entrypoint
COPY entrypoint.sh entrypoint.sh

RUN chmod 755 entrypoint.sh && chown -R jhipster:jhipster /home/jhipster

USER jhipster

EXPOSE 8081

ENTRYPOINT ["./entrypoint.sh"]
