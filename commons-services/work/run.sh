#!/usr/bin/env bash

[ -z $CONSULSERVER_HOST ] && CONSULSERVER_HOST=$(cat /opt/config/ip)

echo "********************************************************"
echo "Waiting for the consul server to start on port $CONSULSERVER_HOST : $CONSULSERVER_PORT"
echo "********************************************************"
./wait-for-it.sh -t 0 $CONSULSERVER_HOST:$CONSULSERVER_PORT -- echo "******* Consul Server has started"

./wait-for-it.sh -t 0 $MQ_ADDR -- echo "******* MQ Server has started"
echo "********************************************************"
echo "Starting @project.build.finalName@ Service with $(hostname -I | cut -d' ' -f1):$SERVER_PORT - $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.cloud.consul.port=$CONSULSERVER_PORT  \
     -Dspring.cloud.consul.host=$CONSULSERVER_HOST               \
     -Dspring.profiles.active=$PROFILE                          \
     -Dspring.r2dbc.url=$DB_URL \
     -Dspring.r2dbc.username=$DB_USERNAME \
     -Dspring.r2dbc.password=$DB_PWD \
     -Dspring.security.oauth2.resourceserver.jwt.issuer-uri=$JWT_ISS \
     -Dspring.cloud.stream.rocketmq.binder.name-server=$MQ_ADDR \
     -Dlocalization.gov-name=$GOV_NAME \
     -jar /usr/local/app/@project.build.finalName@.jar