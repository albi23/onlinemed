FROM openjdk:17
ADD target/onlinemed-mail-sender-1.0.0.jar onlinemed-mail-sender-1.0.0.jar
EXPOSE 8080
RUN echo 'Starting  onlinemed-mail-sender'
CMD java -jar onlinemed-backend-1.0.0.jar
ENTRYPOINT ["java", "--enable-preview", "-XX:TieredStopAtLevel=1", "-Dspring.output.ansi.enabled=always", "-Dspring.jmx.enabled=true", "-Dspring.liveBeansView.mbeanDomain", "-Dfile.encoding=UTF-8","-jar", "onlinemed-mail-sender-1.0.0.jar"]
RUN echo 'Deploying onlinemed-mail-sender finished'