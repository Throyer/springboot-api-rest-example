#!/bin/sh

# Replace variables in nginx conf with
envsubst '${PROXY_REQUESTS_PER_SECOND_LIMIT} ${PROXY_BURST} ${PROXY_PASS_URL}' \
          < /etc/nginx/nginx-proxy.conf > /etc/nginx/nginx.conf

# Start nginx with deamon off so everything is run in the front
nginx -g 'daemon off;'