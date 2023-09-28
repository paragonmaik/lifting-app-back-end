FROM eclipse-temurin:17-jdk-alpine as build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace
COPY /src /workspace/src
RUN ./mvnw -f pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
