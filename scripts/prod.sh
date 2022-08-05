#!/bin/bash

if [ -z "$1" ]; then
  echo "invalid argument"
  exit
fi

if [ $1 = "up" ]; then
  docker-compose -p example-api -f ./docker/docker-compose.prod.yml --env-file ./docker/.env up -d --build
  exit
fi

if [ $1 = "down" ]; then
  docker-compose -p example-api -f ./docker/docker-compose.prod.yml down
  exit
fi

echo "invalid argument"
