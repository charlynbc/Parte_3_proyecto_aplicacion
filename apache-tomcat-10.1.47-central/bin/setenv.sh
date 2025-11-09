#!/bin/bash
# Configuración de Java 17 para Tomcat Central

# Detectar sistema operativo
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS - usar java_home utility
    export JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null)
    if [ -z "$JAVA_HOME" ]; then
        echo "Error: Java 17 no encontrado en Mac"
        echo "Instala con: brew install --cask temurin17"
        exit 1
    fi
else
    # Linux
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
fi

export PATH=$JAVA_HOME/bin:$PATH

# Opciones para headless mode (requerido en Mac)
export CATALINA_OPTS="-Djava.awt.headless=true"
