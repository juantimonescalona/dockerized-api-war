FROM java:8
WORKDIR /
COPY jetty-runner-9.3.7.v20160115.jar /jetty-runner.jar
COPY em-manage.war /app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/jetty-runner.jar"]
CMD ["/app.war"]