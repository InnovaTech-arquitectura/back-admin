# Usar una imagen base que tenga Maven y Java 17 preinstalados
FROM maven:3.8.5-openjdk-17-slim AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos del proyecto Maven al contenedor
COPY demo/pom.xml .
COPY demo/src ./src

# Ejecutar la compilación de Maven (esto incluye 'mvn clean install')
RUN mvn clean install

# Segunda etapa: usar solo el JAR generado
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo para el runtime de la app
WORKDIR /app

# Copiar el JAR generado desde la etapa anterior
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
