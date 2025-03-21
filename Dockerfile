FROM openjdk:17-slim

ENV DEBIAN_FRONTEND=noninteractive

WORKDIR /app
VOLUME /tmp
EXPOSE 8081

ARG VERSION

COPY build/libs/rag-civia-mandate-service-$VERSION.jar /app/app.jar
ENV JAVA_OPTS=""
CMD ["java", "-jar", "app.jar"]