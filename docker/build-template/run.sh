#!/usr/bin/env bash


#[ -z $CONSULSERVER_HOST ] && CONSULSERVER_HOST=$(cat /opt/config/ip)
echo "********************************************************"
echo "Waiting for the Consul server to start on port $CONSUL_HOST:$CONSUL_PORT"
echo "********************************************************"
./wait-for-it.sh -t 0 $CONSUL_HOST:$CONSUL_PORT -- echo "******* Consul Server has started"
echo "********************************************************"
echo "Waiting for the Rocket MQ server to start on port $MQ_ADDR"
echo "********************************************************"
./wait-for-it.sh -t 0 $MQ_ADDR -- echo "******* MQ Server has started"
echo "********************************************************"
echo "Waiting for the Keycloak server to start on port $KEYCLOAK_HOST : $KEYCLOAK_PORT"
echo "********************************************************"
./wait-for-it.sh -t 0 $KEYCLOAK_HOST:$KEYCLOAK_PORT -- echo "******* Keycloak Server has started"

echo "********************************************************"
echo "Starting @project.build.finalName@ Service with $(hostname -I | cut -d' ' -f1):$SERVER_PORT - $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.cloud.consul.port=$CONSUL_PORT  \
     -Dspring.cloud.consul.host=$CONSUL_HOST  \
     -Dspring.profiles.active=$PROFILE       \
     -jar /usr/local/app/@project.build.finalName@.jar