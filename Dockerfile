FROM maven AS build

#create the working directory for the application and copying necessary files
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

#copy java source code and pre-environment configuration files of the artifact
COPY target target

#
# Package stage
#
FROM openjdk
COPY --from=build /workspace/app/target/searchAPI-0.0.1-SNAPSHOT.jar searchAPI.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","searchAPI.jar"]

#compile and run the app packages
CMD ["./mvnw", "spring-boot:run"]