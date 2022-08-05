#!/bin/bash

if [ -z "$1" ]; then
  echo "invalid argument"
  exit
fi

if [ $1 = "up" ]; then
  docker-compose -p example-api-development -f ./docker/docker-compose.dev.yml --env-file ./docker/.env up -d --force-recreate
  exit
fi

if [ $1 = "down" ]; then
  docker-compose -p example-api-development -f ./docker/docker-compose.dev.yml down
  exit
fi

echo "invalid argument"
