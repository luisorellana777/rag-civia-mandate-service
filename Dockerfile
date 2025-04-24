FROM openjdk:17-slim

ENV DEBIAN_FRONTEND=noninteractive

WORKDIR /app
VOLUME /tmp
EXPOSE 8088

ARG VERSION

COPY build/libs/rag-civia-mandate-service-$VERSION.jar /app/app.jar
COPY firebase-service-account.json /app/firebase-service-account.json
ENV JAVA_OPTS=""
CMD ["java", "-jar", "app.jar"]