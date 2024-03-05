FROM openjdk:21
ADD target/onlinemed-mail-sender-1.0.0.jar onlinemed-mail-sender-1.0.0.jar
EXPOSE 8080
RUN echo 'Starting  onlinemed-mail-sender'
ENTRYPOINT ["java", "-XX:TieredStopAtLevel=1", "-Dspring.output.ansi.enabled=always", "-Dspring.jmx.enabled=true", "-Dspring.liveBeansView.mbeanDomain", "-Dfile.encoding=UTF-8","-jar", "onlinemed-mail-sender-1.0.0.jar",  "--enable-preview"]
RUN echo 'Deploying onlinemed-mail-sender finished'