#!/usr/bin/env bash


IFS=";"

read -a items <<< "$WAIT_SERVICES"

if [ ${#items[@]} -eq 1 ]; then
  echo "********************************************************"
  echo "Waiting for the server to start on ${items[0]}"
  echo "********************************************************"
  ./wait-for-it.sh -t 0 ${items[0]} -- echo "*******  ${items[0]} Server has started"
  echo "${items[0]} SERVER IS UP"
else
  # 有分号，遍历数组中的每个字段
  for item in "${items[@]}"; do
    echo "Processing item: $item"
    echo "********************************************************"
    echo "Waiting for the server to start on $item"
    echo "********************************************************"
    ./wait-for-it.sh -t 0 $item -- echo "*******  $item Server has started"
    echo "$item SERVER IS UP"
  done
fi


echo "********************************************************"
echo "Starting @project.build.finalName@ Service with $(hostname -I | cut -d' ' -f1):$SERVER_PORT - $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.cloud.consul.port=$CONSUL_PORT  \
     -Dspring.cloud.consul.host=$CONSUL_HOST  \
     -Dspring.profiles.active=$PROFILE       \
     -jar /usr/local/app/@project.build.finalName@.jar