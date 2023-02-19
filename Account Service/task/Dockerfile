FROM eclipse-temurin:17.0.6_10-jre-alpine
ENV LC_ALL en_US.UTF-8
#RUN mkdir -p /account-service-app
WORKDIR /account-service-app
COPY ./build/libs/Account_Service-task.jar account-service.jar
ENTRYPOINT ["java", "-jar", "account-service.jar"]
EXPOSE 28852