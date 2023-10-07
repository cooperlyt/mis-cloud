#!/usr/bin/env bash


#[ -z $CONSULSERVER_HOST ] && CONSULSERVER_HOST=$(cat /opt/config/ip)
echo "********************************************************"
echo "Waiting for the db and keycloak server to start on port 3306"
echo "********************************************************"
#./wait-for-it.sh -t 0 database:3306 -- echo "******* db Server has started"
./wait-for-it.sh -t 0 keycloak:8443 -- echo "******* keycloak Server has started"

echo "********************************************************"
echo "Starting @project.build.finalName@ Service with $(hostname -I | cut -d' ' -f1):$SERVER_PORT - $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.profiles.active=$PROFILE                          \
     -Dspring.datasource.url=$DB_URL \
     -Dspring.datasource.username=$DB_USERNAME \
     -Dspring.datasource.password=$DB_PWD \
     -jar /usr/local/app/@project.build.finalName@.jar