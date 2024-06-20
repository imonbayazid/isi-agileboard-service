FROM openjdk:21-jdk-slim
EXPOSE 8080:8080

ENV APPLICATION_USER ktor
RUN adduser --shell /bin/sh --disabled-password --gecos "" $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY build/libs/*.jar /app/app.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]

