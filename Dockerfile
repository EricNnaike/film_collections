
FROM openjdk:17
RUN touch /env.txt
RUN printenv > /env.txt
COPY target/FILMCOLLECTION-0.0.1-SNAPSHOT.jar filmcollection-0.0.1-snapshot.jar
ENTRYPOINT ["java","-jar","/filmcollection-0.0.1-snapshot.jar"]