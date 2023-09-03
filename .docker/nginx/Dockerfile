FROM nginx:1.25.2-alpine

ENV PROXY_REQUESTS_PER_SECOND_LIMIT 10
ENV PROXY_BURST 50
ENV PROXY_PASS_URL https://api.ipify.org/?format=json

ENV DOCKERIZE_VERSION v0.7.0

RUN apk update --no-cache \
    && apk add --no-cache wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apk del wget

ADD nginx.conf /etc/nginx/nginx-proxy.conf

ADD entry.sh /entry.sh

WORKDIR /

RUN ["chmod", "+x", "/entry.sh"]

CMD ["/entry.sh"]