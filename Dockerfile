FROM eclipse-temurin:21-jre-ubi9-minimal

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

ARG APP_NAME
ARG VERSION

# Add a jhipster user to run our application so that it doesn't need to run as root
RUN adduser -M -s /bin/sh jhipster
RUN ls -lah
RUN ls -lah home
RUN ls -lah etc
RUN ls -lah tmp
RUN ls -lah usr

WORKDIR /home/jhipster


COPY entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown jhipster:jhipster entrypoint.sh
USER jhipster

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8081

COPY ./*.jar app.jar

