FROM eclipse-temurin:21

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8761

ENTRYPOINT ["java","-jar","target/ServiceRegistry-0.0.1-SNAPSHOT.jar"]