#!/usr/bin/env bash

#get_arch=`arch`
#docker build -f Dockerfile-$get_arch -t @docker.image.name@:$get_arch-2.1.8-snapshot .

if [ "$1" == "test" ] || [ "$1" != "publish" ] && ( [[ "2.1.8-snapshot" == *"snapshot" ]] || [[ "2.1.8-snapshot" == *"SNAPSHOT" ]] )
then
  docker build -t coopersoft/dictionary:2.1.8-snapshot .
else
  docker buildx build . --platform linux/amd64,linux/arm64 --push -t coopersoft/dictionary:2.1.8-snapshot
fi