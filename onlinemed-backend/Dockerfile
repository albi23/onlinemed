FROM openjdk:15.0.1-oracle
ADD target/onlinemed-backend-1.0.0.jar onlinemed-backend-1.0.0.jar
EXPOSE 8080
RUN echo 'Start spp'
CMD java -jar onlinemed-backend-1.0.0.jar
ENTRYPOINT ["java", "--enable-preview", "-XX:TieredStopAtLevel=1", "-noverify", "-Dspring.output.ansi.enabled=always", "-Dspring.jmx.enabled=true", "-Dspring.liveBeansView.mbeanDomain", "-Dspring.application.admin.enabled=true", "-Dfile.encoding=UTF-8","-jar", "onlinemed-backend-1.0.0.jar"]