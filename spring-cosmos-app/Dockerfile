FROM mcr.microsoft.com/azure-functions/java:4-java17-build AS installer-env

COPY . /src/java-function-app
RUN cd /src/java-function-app && \
    mkdir -p /home/site/wwwroot && \
    mvn clean install -DskipTests && \
    cd ./target/azure-functions/ && \
    cd $(ls -d */|head -n 1) && \
    cp -a . /home/site/wwwroot && \
    cd /home/site/wwwroot && \
    curl -SL --output opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
 
# This Zulu OpenJDK Dockerfile and corresponding Docker image are
# to be used solely with Java applications or Java application components
# that are being developed for deployment on Microsoft Azure or Azure Stack,
# and are not intended to be used for any other purpose.
# This image is ssh enabled
FROM mcr.microsoft.com/azure-functions/java:4-java17-appservice

# If you want to deploy outside of Azure, use this image
#FROM mcr.microsoft.com/azure-functions/4-java17

ENV AzureWebJobsScriptRoot=/home/site/wwwroot \
    AzureFunctionsJobHost__Logging__Console__IsEnabled=true

COPY --from=installer-env ["/home/site/wwwroot", "/home/site/wwwroot"]