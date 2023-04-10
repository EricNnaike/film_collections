#FROM openjdk:17
#EXPOSE 9001
#ADD target/filmcollection-0.0.1-snapshot.jar filmcollection-0.0.1-snapshot.jar
#ENTRYPOINT ["java", "-jar", "/filmcollection-0.0.1-snapshot.jar"]

FROM openjdk:17
RUN touch /env.txt
RUN printenv > /env.txt
COPY target/FILMCOLLECTION-0.0.1-SNAPSHOT.jar filmcollection-0.0.1-snapshot.jar
ENTRYPOINT ["java","-jar","/filmcollection-0.0.1-snapshot.jar"]