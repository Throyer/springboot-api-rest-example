if [ -z "$1" ]; then
  echo "invalid argument"
  return
fi

if [ $1 = "up" ]; then
  docker-compose -p example-api -f ./docker/docker-compose.prod.yml --env-file ./docker/.env up -d --build
  return
fi

if [ $1 = "down" ]; then
  docker-compose -p example-api -f ./docker/docker-compose.prod.yml down
  return
fi

echo "invalid argument"
