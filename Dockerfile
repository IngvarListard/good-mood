FROM openjdk:8-alpine

COPY target/uberjar/good-mood.jar /good-mood/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/good-mood/app.jar"]
