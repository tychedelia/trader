FROM openjdk:8-alpine

COPY target/uberjar/trader.jar /trader/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/trader/app.jar"]
