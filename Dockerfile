# syntax = docker/dockerfile:1.0-experimental

# 1. Build image
FROM clojure:openjdk-8-lein AS target

WORKDIR /build

# install node
RUN rm -f /etc/apt/apt.conf.d/docker-clean; echo 'Binary::apt::APT::Keep-Downloaded-Packages "true";' > /etc/apt/apt.conf.d/keep-cache
RUN --mount=type=cache,target=/var/cache/apt --mount=type=cache,target=/var/lib/apt \
  curl -sL https://deb.nodesource.com/setup_13.x | bash - && \
  apt update && \
  apt-get --no-install-recommends install -y nodejs npm

# build and cache deps
COPY . .
RUN --mount=type=cache,target=/root/.m2 lein uberjar

# 2. Run image
FROM openjdk:8

COPY --from=target /build/target/uberjar/trader.jar /trader/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/trader/app.jar"]
