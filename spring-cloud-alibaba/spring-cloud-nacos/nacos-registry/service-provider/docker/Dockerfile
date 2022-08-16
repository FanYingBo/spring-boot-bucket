FROM openjdk:8
ARG PROJECT_VERSION
ARG PROJECT_NAME
ADD ${PROJECT_NAME}-${PROJECT_VERSION}.jar /opt/service-provider.jar
RUN echo "Project Name": ${PROJECT_NAME}
RUN echo "Project Version": ${PROJECT_VERSION}
ENV JAVA_OPTS='-Xms125M -Xmx125M -XX:+UseParallelGC'
ENV SERVER_PORT=8080
ENTRYPOINT java ${JAVA_OPTS} -Dserver.port=${SERVER_PORT} -jar /opt/service-provider.jar
