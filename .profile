#!/bin/bash

SERVICE_INSTANCE_NAME=atrs-db
CREDS=$(echo $VCAP_SERVICES | jq -r ".[] | map(select(.name == \"${SERVICE_INSTANCE_NAME}\"))[0].credentials")

export URL=$(echo $CREDS | jq -r .uri)
export DATABASE_USERNAME=$(echo $URL | awk -F '//' '{print $2}' | awk -F ':' '{print $1}')
export DATABASE_PASSWORD=$(echo $URL | awk -F '//' '{print $2}' | awk -F ':' '{print $2}' | awk -F '@' '{print $1}')
export DATABASE_URL=$(echo $URL | sed 's/postgres/jdbc:postgresql/g' | sed "s/$DATABASE_USERNAME:$DATABASE_PASSWORD@//g")

if [ "${INSTANCE_INDEX}" == "0" ];then
  ./.java-buildpack/open_jdk_jre/bin/java \
    -cp `ls ./WEB-INF/lib/postgresql*.jar`:./flyway/lib/* \
    org.flywaydb.commandline.Main migrate \
    -url=${DATABASE_URL} \
    -user=${DATABASE_USERNAME} \
    -password=${DATABASE_PASSWORD}
fi
