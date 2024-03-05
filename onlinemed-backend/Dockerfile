FROM openjdk:17
ADD target/onlinemed-backend-1.0.0.jar onlinemed-backend-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "--add-opens", "java.base/jdk.internal.reflect=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-XX:TieredStopAtLevel=1", "-Dspring.output.ansi.enabled=always", "-Dspring.jmx.enabled=true", "-Dspring.liveBeansView.mbeanDomain", "-Dspring.application.admin.enabled=true", "-Dfile.encoding=UTF-8", "-jar", "onlinemed-backend-1.0.0.jar", "--enable-preview"]
RUN echo 'Starting  onlinemed-service '
