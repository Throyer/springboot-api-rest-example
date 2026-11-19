FROM eclipse-temurin:25-jdk

ENV MAVEN_VERSION=3.10.1
ENV MAVEN_SHA512=8af2fcc48ee6b8f8fc0b21054226e14404b88500df6483cdeb3ae603408f6de27b1397ac591a9fb0d98fee64f4f624fee1383efd25b8998786cda1598fb9ea39
ENV MAVEN_HOME=/usr/share/maven
ENV DOCKERIZE_VERSION=v0.7.0

RUN apt-get update \
    && apt-get install -y --no-install-recommends wget \
    && wget -q -O /tmp/maven.tar.gz https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    && echo "$MAVEN_SHA512  /tmp/maven.tar.gz" | sha512sum -c - \
    && mkdir -p $MAVEN_HOME \
    && tar -xzf /tmp/maven.tar.gz -C $MAVEN_HOME --strip-components=1 \
    && ln -s $MAVEN_HOME/bin/mvn /usr/bin/mvn \
    && rm /tmp/maven.tar.gz \
    && wget -q -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apt-get autoremove -yqq --purge wget && rm -rf /var/lib/apt/lists/*

WORKDIR /app
