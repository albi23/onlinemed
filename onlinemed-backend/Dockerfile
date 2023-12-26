FROM openjdk:17
ADD target/onlinemed-backend-1.0.0.jar onlinemed-backend-1.0.0.jar
EXPOSE 8080
RUN echo 'Start app'
CMD java -jar onlinemed-backend-1.0.0.jar
ENTRYPOINT ["java", "--enable-preview", "-XX:TieredStopAtLevel=1", "-noverify", "-Dspring.output.ansi.enabled=always", "-Dspring.jmx.enabled=true", "-Dspring.liveBeansView.mbeanDomain", "-Dspring.application.admin.enabled=true", "-Dfile.encoding=UTF-8","-jar", "onlinemed-backend-1.0.0.jar"]