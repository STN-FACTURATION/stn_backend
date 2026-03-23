#!/bin/sh

echo "The application will start in ${JHIPSTER_SLEEP}s..., profile : ${PROFILE}" && sleep ${JHIPSTER_SLEEP}
echo "Running java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar app.jar" "$@"

exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "app.jar" "$@"
