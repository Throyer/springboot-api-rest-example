if [ -z "$1" ]; then
  echo "invalid argument"
  return
fi

if [ $1 = "up" ]; then
  docker-compose -p example-api-development -f ./docker/docker-compose.dev.yml --env-file ./docker/.env up -d --force-recreate
  return
fi

if [ $1 = "down" ]; then
  docker-compose -p example-api-development -f ./docker/docker-compose.dev.yml down
  return
fi

echo "invalid argument"
