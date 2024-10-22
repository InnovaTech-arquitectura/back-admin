#!/bin/bash

echo "Iniciando proceso de despliegue..."

# Cambiar al directorio donde se clonará el repositorio
cd /home/estudiante/Desktop || exit 1

# Clonar el repositorio (si ya existe, lo eliminamos primero)
if [ -d "back-admin" ]; then
    echo "Eliminando el directorio existente de back-admin..."
    rm -rf back-admin
fi

echo "Clonando el repositorio..."
git clone https://github.com/InnovaTech-arquitectura/back-admin.git

# Cambiar al directorio del repositorio clonado
cd back-admin || exit 1

# Detener y eliminar los contenedores existentes
echo "Deteniendo y eliminando contenedores existentes..."
docker-compose down --remove-orphans  # Asegúrate de detener y eliminar contenedores huérfanos

# Eliminar cualquier contenedor existente por si acaso (en caso de que el comando anterior no los elimine)
echo "Eliminando contenedores existentes..."
docker rm -f $(docker ps -aq)  # Elimina todos los contenedores, aunque estén detenidos

# Ejecutar docker-compose con el argumento de entorno
echo "Ejecutando docker-compose..."
docker-compose up --build -d

echo "Despliegue completado."
