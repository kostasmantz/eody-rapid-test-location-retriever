FROM openjdk:11-jdk-slim
#
#RUN addgroup -S eody-poi-app && adduser -S eody-poi-app -G eody-poi-app
#USER eody-poi-app:eody-poi-app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} eody-sites-retriever.jar

ENTRYPOINT ["java","-jar","/eody-sites-retriever.jar"]